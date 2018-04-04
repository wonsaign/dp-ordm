package com.zeusas.dp.ordm.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.zeusas.core.utils.AppContext;
import com.zeusas.dp.ordm.data.entity.DwtOrg;
import com.zeusas.dp.ordm.data.mapper.ResultSetMapper;
import com.zeusas.dp.ordm.data.utils.PathUtil;

/**
 * 
 * @author shihx
 * @date 2016年12月9日 下午4:03:09
 */
public class OrgSynchronization {

	private DataSource DS;
	private DataSource dwtDS;
	static Logger logger = LoggerFactory.getLogger(OrgSynchronization.class);

	/**
	 * get Datasouce
	 */
	public void innitDatasouce() {
		ApplicationContext ctx = new FileSystemXmlApplicationContext("config/zeus-test.xml");
		DS = (DataSource) AppContext.getBean("dataSource");
		dwtDS = (DataSource) AppContext.getBean("dwtdataSource");
	}

	/**
	 * get Connection
	 * 
	 * @param ds
	 * @return
	 * @throws SQLException
	 */
	public Connection getConn(DataSource ds) throws SQLException {
		if (ds != null) {
			Connection conn = ds.getConnection();
			return conn;
		} else {
			return null;
		}
	}

	/**
	 * get org Data from source db
	 * 
	 * @param conn
	 * @return
	 * @throws SQLException
	 */
	public List<DwtOrg> getData(Connection conn) throws SQLException {
		String sql = "select BIN_OrganizationID,DepartCode,DepartName,Path,BIN_ProvinceID,BIN_CityID,"
				+ "BIN_CountyID,Address,Type,ValidFlag,UpdateTime " + " from [CherryBrand_LS].Basis.BIN_Organization "
				+ "where ValidFlag = 1 and TestType = 0 and path not like '%/1/1/1/%' order by Level";

		PreparedStatement pstmt = (PreparedStatement) conn.prepareStatement(sql);
		ResultSet rs = pstmt.executeQuery();

		ResultSetMapper<DwtOrg> resultSetMapper = new ResultSetMapper<DwtOrg>();
		List<DwtOrg> dwtOrgList = resultSetMapper.mapRersultSetToObject(rs, DwtOrg.class);
		return dwtOrgList;
	}

	/**
	 * pu path orgid int map,get pid from map according to orgid
	 * 
	 * @param list
	 * @return
	 */
	public Map<String, Integer> getPath(List<DwtOrg> list) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("/", 0);
		for (DwtOrg dwtOrg : list) {
			map.put(dwtOrg.getPath().trim(), dwtOrg.getOrgID());
		}
		return map;
	}

	/**
	 * set org data to target db
	 * 
	 * @param conn
	 * @param list
	 * @param map
	 * @throws SQLException
	 */
	public void setData(Connection conn, List<DwtOrg> list, Map<String, Integer> map) throws SQLException {
		String sql = "INSERT INTO core_orgunit " + "(orgId, pid, orgCode, commonName, country, province,"
				+ "city, areaCounty, level, Status, lastUpdate)" + "values(?,?,?,?,?,?,?,?,?,?,?)";

		PreparedStatement pstmt = (PreparedStatement) conn.prepareStatement(sql);

		Date date = new Date();
		int Level;
		int status;
		for (DwtOrg dwtOrg : list) {
			pstmt.setInt(1, dwtOrg.getOrgID());// orgId
			try {
				pstmt.setInt(2, map.get(PathUtil.getParentPath(dwtOrg.getPath())));// pid
			} catch (Exception e) {
				logger.info("Exception by orgID:" + dwtOrg.getOrgID() + "  " + dwtOrg.getPath());
			}
			pstmt.setString(3, dwtOrg.getCode());// orgCode
			pstmt.setString(4, dwtOrg.getName());// commonName
			pstmt.setString(5, "中国");// country
			/**
			 * province city areaCounty 数据源 省 市 区 ID与目标表ID不一致
			 */
			pstmt.setString(6, null == dwtOrg.getProvinceId() ? null : dwtOrg.getProvinceId().toString());// province
			pstmt.setString(7, null == dwtOrg.getCityId() ? null : dwtOrg.getCityId().toString());// city
			pstmt.setString(8, null == dwtOrg.getCountyId() ? null : dwtOrg.getCityId().toString());// areaCounty
			/**
			 * --0 集团总部 --1 品牌总部 --7 总部部门 --5 大区 --2 办事处 --3 经销商 --6 柜台主管 --4 柜台
			 */

			switch (dwtOrg.getType()) {
			case "0":
				Level = 10301;
				break;
			case "1":
				Level = 10302;
				break;
			case "7":
				Level = 10303;
				break;
			case "5":
				Level = 10304;
				break;
			case "2":
				Level = 10305;
				break;
			case "3":
				Level = 10306;
				break;
			case "6":
				Level = 10307;
				break;
			case "4":
				Level = 10308;
				break;
			case "Z":
				Level = 10309;
				break;
			default:
				Level = 0;
				break;
			}
			pstmt.setInt(9, Level);// level
			switch (dwtOrg.getValidflag()) {
			case "1":
				status = 10101;
				break;
			case "2":
				status = 10102;
				break;
			default:
				status = 0;
				break;
			}
			pstmt.setInt(10, status);// Status
			pstmt.setLong(11, date.getTime());// lastUpdate

			pstmt.executeUpdate();
		}
	}

	public void Synchronization() throws Exception {
		innitDatasouce();
		Connection conn = getConn(DS);
		logger.info("获取到conn");
		Connection dwtconn = getConn(dwtDS);
		logger.info("获取到dwtconn");

		List<DwtOrg> list = getData(dwtconn);
		Map<String, Integer> map = getPath(list);
		setData(conn, list, map);

		conn.close();
		dwtconn.close();
	}
}
