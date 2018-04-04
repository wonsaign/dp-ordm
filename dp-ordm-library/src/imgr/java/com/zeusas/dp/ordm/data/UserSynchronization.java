package com.zeusas.dp.ordm.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.esotericsoftware.minlog.Log;
import com.zeusas.core.utils.AppContext;
import com.zeusas.dp.ordm.data.entity.DwtUser;
import com.zeusas.dp.ordm.data.mapper.ResultSetMapper;

/**
 * 
 * @author shihx
 * @date 2016年12月9日 下午6:07:08
 */
public class UserSynchronization {

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
	 * 在DepartType级别选取 u.BIN_UserID 与组织节点唯一对应的数据 的id
	 * 
	 * @param conn
	 * @param DepartType
	 * @return
	 * @throws SQLException
	 */
	public List<Integer> getUserId(Connection conn, String DepartType) throws SQLException {
		String sql = "SELECT u.BIN_UserID FROM [CherryBrand_LS].[Privilege].[BIN_User] u "
				+ "inner join [CherryBrand_LS].[Privilege].[BIN_DepartPrivilege] dp on dp.BIN_UserID =u.BIN_UserID "
				+ "inner join [CherryBrand_LS].Basis.BIN_Organization org on org.BIN_OrganizationID = dp.BIN_OrganizationID "
				+ "where u.validflag = 1  and org.validflag = 1  and testtype = '0' and Path not like '%/1/1/1/%' and DepartType = ? "
				+ "GROUP BY u.BIN_UserID HAVING COUNT(u.BIN_UserID)=1";
		PreparedStatement pstmt = (PreparedStatement) conn.prepareStatement(sql);
		pstmt.setString(1, DepartType);
		ResultSet rs = pstmt.executeQuery();
		ResultSetMetaData md = rs.getMetaData();
		List<Integer> list = new ArrayList<Integer>();
		while (rs.next()) {
			list.add(rs.getInt(1));
		}
		return list;
	}

	/**
	 * 在DepartType级别选取 u.BIN_UserID 与组织节点唯一对应的数据
	 * 
	 * @param conn
	 * @param DepartType
	 * @return
	 * @throws SQLException
	 */
	public List<DwtUser> getUserByDepartType(Connection conn, String DepartType) throws SQLException {
		String sql = "SELECT distinct u.BIN_UserID,u.BIN_EmployeeID,LonginName,PassWord,org.BIN_OrganizationID "
				+ "into #departaccount  FROM [CherryBrand_LS].[Privilege].[BIN_User] u "
				+ "inner join [CherryBrand_LS].[Privilege].[BIN_DepartPrivilege] dp on dp.BIN_UserID =u.BIN_UserID "
				+ "inner join [CherryBrand_LS].Basis.BIN_Organization org on org.BIN_OrganizationID = dp.BIN_OrganizationID "
				+ "where u.validflag = 1  and org.validflag = 1  and testtype = '0' and Path not like '%/1/1/1/%'  and [DepartType] = ? "
				+ "SELECT BIN_UserID  INTO #list  FROM #departaccount "
				+ "GROUP BY BIN_UserID HAVING COUNT(BIN_UserID)=1 "
				+ "SELECT * FROM #departaccount  WHERE BIN_UserID IN( SELECT BIN_UserID FROM #list)";

		PreparedStatement pstmt = (PreparedStatement) conn.prepareStatement(sql);
		pstmt.setString(1, DepartType);
		ResultSetMapper<DwtUser> resultSetMapper = new ResultSetMapper<DwtUser>();
		ResultSet rs = pstmt.executeQuery();
		List<DwtUser> list = resultSetMapper.mapRersultSetToObject(rs, DwtUser.class);
		return list;
	}

	/**
	 * insert user date to target db
	 * 
	 * @param conn
	 * @param list
	 * @throws SQLException
	 */
	public void save(Connection conn, List<DwtUser> list) throws SQLException {
		String sql = "INSERT INTO dpos_wms_order.core_authuser (uid, loginName,  password, orgUnit,status, lastUpdate, createTime) VALUES (?,?,?,?,?,?,?)";
		PreparedStatement pstmt = (PreparedStatement) conn.prepareStatement(sql);

		// int status;
		for (DwtUser u : list) {
			pstmt.setInt(1, u.getUserId());// uid
			pstmt.setString(2, u.getLonginName());// loginName
			pstmt.setString(3, u.getPwd());// password
			pstmt.setInt(4, u.getOrgid());// orgUnit
			pstmt.setString(5, "1");// status
			pstmt.setLong(6, System.currentTimeMillis());// lastUpdate
			pstmt.setLong(7, System.currentTimeMillis());// createTime

			pstmt.executeUpdate();
		}
	}

	/**
	 * insert user date to target db
	 * 
	 * @param conn
	 * @param u
	 * @throws SQLException
	 */
	public void save(Connection conn, DwtUser u) throws SQLException {
		String sql = "INSERT INTO core_authuser (uid, loginName,  password, orgUnit,status, lastUpdate, createTime) VALUES (?,?,?,?,?,?,?)";
		PreparedStatement pstmt = (PreparedStatement) conn.prepareStatement(sql);

		pstmt.setInt(1, u.getUserId());// uid
		pstmt.setString(2, u.getLonginName());// loginName
		pstmt.setString(3, u.getPwd());// password
		pstmt.setInt(4, u.getOrgid());// orgUnit
		pstmt.setString(5, "1");// status
		pstmt.setLong(6, System.currentTimeMillis());// lastUpdate
		pstmt.setLong(7, System.currentTimeMillis());// createTime

		pstmt.executeUpdate();
	}

	/**
	 * ipdate user date to target db
	 * 
	 * @param conn
	 * @param u
	 * @throws SQLException
	 */
	public void update(Connection conn, DwtUser u) throws SQLException {
		String sql = "UPDATE core_authuser SET loginName=?,  password=?, orgUnit=?,status=?, lastUpdate=?, createTime=? WHERE uid=?";
		PreparedStatement pstmt = (PreparedStatement) conn.prepareStatement(sql);

		pstmt.setString(1, u.getLonginName());// loginName
		pstmt.setString(2, u.getPwd());// password
		pstmt.setInt(3, null == u.getOrgid() ? 0 : u.getOrgid());// orgUnit
		pstmt.setString(4, "1");// status
		pstmt.setLong(5, System.currentTimeMillis());// lastUpdate
		pstmt.setLong(6, System.currentTimeMillis());// createTime
		pstmt.setInt(7, u.getUserId());// uid

		pstmt.executeUpdate();
	}

	/**
	 * saveorupdate user date to target db
	 * 
	 * @param conn
	 * @param list
	 * @throws SQLException
	 */
	public void saveOrUpdate(Connection conn, List<DwtUser> list) throws SQLException {
		String sql = "SELECT uid FROM core_authuser";
		PreparedStatement pstmt = (PreparedStatement) conn.prepareStatement(sql);
		ResultSet rs = pstmt.executeQuery();
		ResultSetMetaData md = rs.getMetaData();

		Set<Integer> set = new HashSet<Integer>();
		while (rs.next()) {
			set.add(rs.getInt(1));
		}

		for (DwtUser u : list) {
			if (set.contains(u.getUserId())) {
				update(conn, u);
			} else {
				save(conn, u);
			}
		}
	}

	/**
	 * 导数据
	 * 
	 * @throws Exception
	 */
	public void Synchronization() throws Exception {
		innitDatasouce();
		Connection conn = getConn(DS);
		Connection dwtconn = getConn(dwtDS);
		List<String> type=new ArrayList<String>();
		type.add("6");
		type.add("3");
		type.add("2");
		type.add("5");
		type.add("7");
		type.add("1");
		for (String str : type) {
			List<DwtUser> list = getUserByDepartType(dwtconn, str);
			saveOrUpdate(conn, list);
		}
		dwtconn.close();
		conn.close();
	}

	/**
	 * DepartType级别重复用户
	 * 
	 * @throws Exception
	 */
	public void repetition() throws Exception {
		innitDatasouce();
		Connection conn = getConn(dwtDS);
		List<Integer> p = getUserId(conn, "1");
		List<Integer> c = getUserId(conn, "6");
		for (Integer id : p) {
			if (c.contains(id)) {
				logger.info("重复用户"+id);
			}
		}
	}

	public void testName() throws Exception {
		innitDatasouce();
		Connection conn = getConn(DS);
		DwtUser u = new DwtUser();
		u.setUserId(6);
		u.setLonginName("test");
		update(conn, u);
	}
}