package cn.easybike.action;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import cn.easybike.entity.Bike;
import cn.easybike.entity.LendAndReturnRecord;
import cn.easybike.entity.Maintenance;
import cn.easybike.entity.Station;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * action<br>
 * 提供对表现层的接口.
 * 
 * @author 马辉
 * @since JDK1.8
 * @history 2016年11月21日下午5:33:48 马辉 新建
 */
public class LendAndReturnRecordAction extends BaseAction<LendAndReturnRecord> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private JSONObject jsonObject = new JSONObject();
	private JSONArray jsonArray = new JSONArray();
	private String recordSn;
	private String stationSn;
	private String lendStation;
	private String lendStationSn;
	private String lendStationName;
	private String bikeSn;
	private String oldBikeSn;
	private String bike;
	private String studentId;
	private String phoneNumber;
	private String studentName;
	private String lendPersonSn;
	private String returnPersonSn;
	private String returnStationSn;
	private String returnStationName;
	private Timestamp lendDateTime; // 借出时间
	private Timestamp returnDateTime;// 归还时间
	private Boolean isHasReturned;// 是否归还
	private String personSn;
	private String oldStudentId;
	private String returnMark;
	private String q;
	private String oldLendStationSn;
	

	public String queryByPersonSn() {
		String sn = (String) session.get("personSn");
		String hql1 = "";
		String hql2 = "";
		if (bikeSn == null || bikeSn == "") {
			hql1 = "select p from LendAndReturnRecord p where p.lendPerson.personSn='" + sn + "'";
			hql2 = "select count(p) from LendAndReturnRecord p where p.lendPerson.personSn='" + sn + "'";
		} else {
			hql1 = "select p from LendAndReturnRecord p where p.lendPerson.personSn='" + sn + "' and p.bike.bikeSn= '"
					+ bikeSn + "'";
			hql2 = "select count(p) from LendAndReturnRecord p where p.lendPerson.personSn='" + sn
					+ "' and p.bike.bikeSn= '" + bikeSn + "'";
		}
		JSONArray array = new JSONArray();
		for (LendAndReturnRecord lendAndReturnRecord : lendAndReturnRecordService.queryByPage(hql1, page, rows)) {
			JSONObject jo = new JSONObject();
			jo.put("recordSn", lendAndReturnRecord.getRecordSn());
			jo.put("bikeSn", lendAndReturnRecord.getBike().getBikeSn());
			jo.put("studentId", lendAndReturnRecord.getStudentId());
			jo.put("studentName", lendAndReturnRecord.getStudentName());
			jo.put("phoneNumber", lendAndReturnRecord.getPhoneNumber());
			jo.put("lendPerson", lendAndReturnRecord.getLendPerson().getPersonName());
			jo.put("lendDateTime", lendAndReturnRecord.getReturnDateTime().toString());
			jo.put("isHasReturned", lendAndReturnRecord.getIsHasReturned());
			if(lendAndReturnRecord.getReturnPerson()!=null){
				jo.put("returnPerson", lendAndReturnRecord.getReturnPerson().getPersonName());
			}
			if(lendAndReturnRecord.getReturnDateTime()!=null){
				jo.put("returnDateTime", lendAndReturnRecord.getReturnDateTime().toString());
			}
			
			array.add(jo);
		}
		jsonObject.put("rows", array);
		jsonObject.put("total", lendAndReturnRecordService.countByHql(hql2));
		return "jsonObject";
	}

	public String queryByBikeSn() {
		String hql1 = "select p from LendAndReturnRecord p where p.bike.bikeSn='" + bikeSn + "'";
		String hql2 = "select count(p) from LendAndReturnRecord p where p.bike.bikeSn='" + bikeSn + "'";
		JSONArray array = new JSONArray();
		for (LendAndReturnRecord lendAndReturnRecord : lendAndReturnRecordService.queryByPage(hql1, page, rows)) {
			JSONObject jo = new JSONObject();
			jo.put("recordSn", lendAndReturnRecord.getRecordSn());
			jo.put("bikeSn", lendAndReturnRecord.getBike().getBikeSn());
			jo.put("studentId", lendAndReturnRecord.getStudentId());
			jo.put("studentName", lendAndReturnRecord.getStudentName());
			jo.put("phoneNumber", lendAndReturnRecord.getPhoneNumber());
			if(lendAndReturnRecord.getReturnDateTime()!=null){
				jo.put("lendDateTime", lendAndReturnRecord.getReturnDateTime().toString());
			}
			if(lendAndReturnRecord.getLendPerson()!=null){
				jo.put("lendPerson", lendAndReturnRecord.getLendPerson().getPersonName());
			}
			if(lendAndReturnRecord.getReturnDateTime()!=null){
				jo.put("returnDateTime", lendAndReturnRecord.getReturnDateTime().toString());
			}
			
			array.add(jo);
		}
		jsonObject.put("rows", array);
		jsonObject.put("total", lendAndReturnRecordService.countByHql(hql2));
		return "jsonObject";
	}


	// 借车页面的分页查询
	public String queryByPage() {
		String hql = "select r from LendAndReturnRecord r order by r.isHasReturned,r.lendDateTime desc";
		JSONArray array = new JSONArray();
		for (LendAndReturnRecord lendAndReturnRecord : lendAndReturnRecordService.queryByPage(hql, page, rows)) {
			JSONObject jo = new JSONObject();
			jo.put("recordSn", lendAndReturnRecord.getRecordSn());
			if(lendAndReturnRecord.getLendStation()!=null){
				jo.put("lendStationSn", lendAndReturnRecord.getLendStation().getStationSn());
				jo.put("lendStationName", lendAndReturnRecord.getLendStation().getStationName());
			}
			
			jo.put("bikeSn", lendAndReturnRecord.getBike().getBikeSn());
			jo.put("studentId", lendAndReturnRecord.getStudentId());
			jo.put("studentName", lendAndReturnRecord.getStudentName());
			jo.put("phoneNumber", lendAndReturnRecord.getPhoneNumber());
			Timestamp timestamp = lendAndReturnRecord.getLendDateTime();
			if (timestamp != null) {
				String timestamp2 = timestamp.toString();
				jo.put("lendDateTime", timestamp2);
			} else {
				jo.put("lendDateTime", "");
			}
			jo.put("isHasReturned", lendAndReturnRecord.getIsHasReturned());
			jo.put("lendPersonSn", lendAndReturnRecord.getLendPerson().getPersonSn());
			jo.put("lendPersonName", lendAndReturnRecord.getLendPerson().getPersonName());
			array.add(jo);
		}
		jsonObject.put("total", lendAndReturnRecordService.countByHql("select count(l) from LendAndReturnRecord l"));
		jsonObject.put("rows", array);
		return "jsonObject";
	}

	// 还车页面
	public String queryByPage2() {
		String hql = "select a from LendAndReturnRecord a";
		JSONArray array = new JSONArray();
		if (studentId != null && studentId.trim().length() > 0) {
			hql += " where a.studentId like '%" + studentId + "%'";
		}
		hql+=" order by a.isHasReturned,a.lendDateTime desc";
		for (LendAndReturnRecord lendAndReturnRecord : lendAndReturnRecordService.queryByPage(hql, page, rows)) {
			JSONObject jo = new JSONObject();
			jo.put("recordSn", lendAndReturnRecord.getRecordSn());
			jo.put("lendStationSn", lendAndReturnRecord.getLendStation().getStationSn());
			jo.put("lendStationName", lendAndReturnRecord.getLendStation().getStationName());
			jo.put("bikeSn", lendAndReturnRecord.getBike().getBikeSn());
			jo.put("studentId", lendAndReturnRecord.getStudentId());
			jo.put("studentName", lendAndReturnRecord.getStudentName());
			jo.put("phoneNumber", lendAndReturnRecord.getPhoneNumber());
			Timestamp timestamp = lendAndReturnRecord.getLendDateTime();
			if (timestamp != null) {
				String timestamp2 = timestamp.toString();
				jo.put("lendDateTime", timestamp2);
			} else {
				jo.put("lendDateTime", "");
			}
			jo.put("isHasReturned", lendAndReturnRecord.getIsHasReturned());
			jo.put("lendPerson", lendAndReturnRecord.getLendPerson().getPersonSn());
			jo.put("lendPersonName", lendAndReturnRecord.getLendPerson().getPersonName());
			if (lendAndReturnRecord.getReturnPerson() != null) {
				jo.put("returnPersonName", lendAndReturnRecord.getReturnPerson().getPersonName());
				jo.put("returnPersonSn", lendAndReturnRecord.getReturnPerson().getPersonSn());
			}
			Timestamp timestamp3 = lendAndReturnRecord.getReturnDateTime();
			if (timestamp3 != null) {
				String timestamp4 = timestamp3.toString();
				jo.put("returnDateTime", timestamp4);
			} else {
				jo.put("returnDateTime", "");
			}
			if (lendAndReturnRecord.getReturnStation() != null) {
				jo.put("returnStationName", lendAndReturnRecord.getReturnStation().getStationName());
			}
			jo.put("returnMark", lendAndReturnRecord.getReturnMark());
			array.add(jo);
		}
		jsonObject.put("total", lendAndReturnRecordService.countByHql(hql.replaceFirst("a", "count(a)")));
		jsonObject.put("rows", array);
		return "jsonObject";
	}

	// 下拉框获取bike
	public String getAllBike() {
		for (Bike bike : bikeService.queryAll()) {
			JSONObject jo = new JSONObject();
			jo.put("bikeSn", bike.getBikeSn());
			jo.put("id", bike.getId());
			jsonArray.add(jo);
		}
		return "jsonArray";
	}

	// 借车下拉框获取station
	public String getAllStation() {
		for (Station station : stationService.queryAll()) {
			JSONObject jo = new JSONObject();
			jo.put("lendStationSn", station.getStationSn());
			jo.put("stationName", station.getStationName());
			jsonArray.add(jo);
		}
		return "jsonArray";
	}

	// 还车下拉框获取station
	public String getAllStation2() {
		for (Station station : stationService.queryAll()) {
			JSONObject jo = new JSONObject();
			jo.put("returnStationSn", station.getStationSn());
			jo.put("returnStationName", station.getStationName());
			jsonArray.add(jo);
		}
		return "jsonArray";
	}

	public String delete() {
		jsonObject.put("status", "ok");
		try {
			lendAndReturnRecordService.deleteByRecordSn(recordSn);
		} catch (Exception e) {
			jsonObject.put("status", "nook");
		}
		return "jsonObject";
	}

	public String save() {
		jsonObject.put("status", "ok");
		LendAndReturnRecord lendAndReturnRecord = new LendAndReturnRecord();
		try {
			lendAndReturnRecord.setStudentName(studentName);
			lendAndReturnRecord.setStudentId(studentId);
			lendAndReturnRecord.setPhoneNumber(phoneNumber);
			lendAndReturnRecord.setLendStation(stationService.getByStationSn(stationSn));
			lendAndReturnRecord.setBike(bikeService.getByBikeSn(bikeSn));
			
			String recordSn=new SimpleDateFormat("yyyyMMddHHmmssSSS") .format(new Date() );
			lendAndReturnRecord.setRecordSn(recordSn);
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			lendAndReturnRecord.setLendDateTime(Timestamp.valueOf(sdf.format(System.currentTimeMillis())));

			lendAndReturnRecord.setIsHasReturned(false);
			lendAndReturnRecord.setLendPerson(personService.getByPersonSn((String) session.get("personSn")));
			lendAndReturnRecordService.save(lendAndReturnRecord);

			Bike bike = bikeService.getByBikeSn(bikeSn);
			bike.setStation(null);
			bike.setStatus((byte) 1);
			bikeService.update(bike);

		} catch (Exception e) {
			jsonObject.put("status", "nook");
		}
		return "jsonObject";
	}

	// 借车页面的录入
	public String update() {
		jsonObject.put("status", "ok");
		LendAndReturnRecord lendAndReturnRecord = lendAndReturnRecordService.getByRecordSn(recordSn);
		if (lendAndReturnRecord != null) {
			try {
				Bike newBike = bikeService.getByBikeSn(bikeSn);
				lendAndReturnRecord.setStudentId(studentId);
				lendAndReturnRecord.setStudentName(studentName);
				lendAndReturnRecord.setPhoneNumber(phoneNumber);
				lendAndReturnRecord.setLendStation(stationService.getByStationSn(lendStationSn));
				lendAndReturnRecord.setBike(newBike);
				lendAndReturnRecordService.update(lendAndReturnRecord);
				
				//旧车
				Bike oldBike = bikeService.getByBikeSn(oldBikeSn);
				oldBike.setStatus((byte) 0);
				oldBike.setStation(stationService.getByStationSn(oldLendStationSn));
				bikeService.update(oldBike);
				
				newBike.setStatus((byte) 1);
				newBike.setStation(null);
				bikeService.update(newBike);

			} catch (Exception e) {
				jsonObject.put("status", "nook");
			}
		} else {
			jsonObject.put("status", "nook");
		}
		return "jsonObject";
	}

	// 还车页面的录入
	public String update2() {
		jsonObject.put("status", "ok");
		LendAndReturnRecord lendAndReturnRecord = lendAndReturnRecordService.getByRecordSn(recordSn);
		Bike bike = bikeService.getByBikeSn(bikeSn);
		Station station=stationService.getByStationSn(returnStationSn);
		if(lendAndReturnRecord != null){
			try {
				lendAndReturnRecord.setIsHasReturned(true);
				lendAndReturnRecord.setReturnPerson(personService.getByPersonSn((String) session.get("personSn")));
				lendAndReturnRecord.setReturnStation(station);
				lendAndReturnRecord.setReturnMark(returnMark);
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				lendAndReturnRecord.setReturnDateTime(Timestamp.valueOf(sdf.format(System.currentTimeMillis())));
				lendAndReturnRecordService.update(lendAndReturnRecord);
				// 自行车设置为可借状态
				bike.setStatus((byte) 0);
				bike.setStation(station);
				bikeService.update(bike);
			} catch (Exception e) {
				jsonObject.put("status", "nook");
			}
		}else{
			jsonObject.put("status", "took");
		}
		return "jsonObject";
	}

	public String isExist() {
		if (lendAndReturnRecordService.getByStudentId(studentId) != null) {
			jsonObject.put("isExist", true);
		} else {
			jsonObject.put("isExist", false);
		}
		return "jsonObject";
	}

	// 按照bikeSn搜索
	public String queryByQ() {
		String hql = "select b from Bike b where b.bikeSn like '%" + q + "%' and b.status=0";
		for (Bike bike : bikeService.queryByPage(hql, 1,15)) {
			if (bike.getStatus() == (byte) 0) {
				JSONObject jo = new JSONObject();
				jo.put("bikeSn", bike.getBikeSn());
				jsonArray.add(jo);
			}
		}
		return "jsonArray";
	}

	public String getOldBikeSn() {
		return oldBikeSn;
	}

	public void setOldBikeSn(String oldBikeSn) {
		this.oldBikeSn = oldBikeSn;
	}

	public String getQ() {
		return q;
	}

	public void setQ(String q) {
		this.q = q;
	}

	public String getReturnMark() {
		return returnMark;
	}

	public void setReturnMark(String returnMark) {
		this.returnMark = returnMark;
	}

	public String getLendStationName() {
		return lendStationName;
	}

	public void setLendStationName(String lendStationName) {
		this.lendStationName = lendStationName;
	}

	public String getReturnStationName() {
		return returnStationName;
	}

	public void setReturnStationName(String returnStationName) {
		this.returnStationName = returnStationName;
	}

	public String getLendStation() {
		return lendStation;
	}

	public void setLendStation(String lendStation) {
		this.lendStation = lendStation;
	}

	public String getBike() {
		return bike;
	}

	public void setBike(String bike) {
		this.bike = bike;
	}

	public String getReturnStationSn() {
		return returnStationSn;
	}

	public void setReturnStationSn(String returnStationSn) {
		this.returnStationSn = returnStationSn;
	}

	public String getOldStudentId() {
		return oldStudentId;
	}

	public void setOldStudentId(String oldStudentId) {
		this.oldStudentId = oldStudentId;
	}

	public JSONArray getJsonArray() {
		return jsonArray;
	}

	public void setJsonArray(JSONArray jsonArray) {
		this.jsonArray = jsonArray;
	}

	public String getStationSn() {
		return stationSn;
	}

	public void setStationSn(String stationSn) {
		this.stationSn = stationSn;
	}

	public String getLendStationSn() {
		return lendStationSn;
	}

	public void setLendStationSn(String lendStationSn) {
		this.lendStationSn = lendStationSn;
	}

	public String getBikeSn() {
		return bikeSn;
	}

	public void setBikeSn(String bikeSn) {
		this.bikeSn = bikeSn;
	}

	public String getLendPersonSn() {
		return lendPersonSn;
	}

	public void setLendPersonSn(String lendPersonSn) {
		this.lendPersonSn = lendPersonSn;
	}

	public String getReturnPersonSn() {
		return returnPersonSn;
	}

	public void setReturnPersonSn(String returnPersonSn) {
		this.returnPersonSn = returnPersonSn;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRecordSn() {
		return recordSn;
	}

	public void setRecordSn(String recordSn) {
		this.recordSn = recordSn;
	}

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public Timestamp getLendDateTime() {

		return lendDateTime;
	}

	public void setLendDateTime(Timestamp lendDateTime) {
		this.lendDateTime = lendDateTime;
	}

	public Timestamp getReturnDateTime() {
		return returnDateTime;
	}

	public void setReturnDateTime(Timestamp returnDateTime) {
		this.returnDateTime = returnDateTime;
	}

	public Boolean getIsHasReturned() {
		return isHasReturned;
	}

	public void setIsHasReturned(Boolean isHasReturned) {
		this.isHasReturned = isHasReturned;
	}
	public JSONObject getJsonObject() {
		return jsonObject;
	}

	public String getPersonSn() {
		return personSn;
	}

	public void setJsonObject(JSONObject jsonObject) {
		this.jsonObject = jsonObject;
	}

	public void setPersonSn(String personSn) {
		this.personSn = personSn;
	}
	public String getOldLendStationSn() {
		return oldLendStationSn;
	}

	public void setOldLendStationSn(String oldLendStationSn) {
		this.oldLendStationSn = oldLendStationSn;
	}
}
