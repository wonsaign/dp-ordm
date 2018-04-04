package com.zeusas.dp.ordm.entity;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;

import com.alibaba.fastjson.JSON;
import com.zeusas.core.entity.IEntity;
import com.zeusas.core.utils.BeanDup;

/**
 * Product entity. @author fx 从金蝶同步过来的产品实体 select * from base_product where
 * typeid = 1723 LIMIT 2 , select * from base_product where typeid = 670 limit 2
 * ,
 * 
 * select * from base_product where typeid = 893 limit 2;
 */
@Entity
@Table(name = "base_product")
public class Product implements IEntity, Cloneable {

	private static final long serialVersionUID = 8668738835086519701L;

	public final static String TYPEID_METERIAL = "11389";
	public final static String TYPEID_PRESENT = "11388";
	public final static String TYPEID_PRODUCT = "11386";

	// 正价商品系列
	public final static String PRODUCT_POSITIVE_SERIES = "104";
	// 赠品商品系列
	// public final static String PRODUCT_GIFT_SERIES = "105";
	// 大类商品
	public final static String PRODUCT_BIG_TYPE = "107";
	public static final String PRODUCT_BODY_TYPE = "108";
	public static final String PRODUCT_TYPE = "102";
	public static final String PRODUCT_PRESENT_TYPE = "109";
	public static final String PRODUCT_MATERIEL_TYPE = "110";

	public static final String PRODUCT_NEWPRODUCT = "1";
	public static final String PRODUCT_GLOBALSELLER = "2";
	public static final String PRODUCT_COUNTERSELLER = "3";
	public static final String PRODUCT_GROUP = "4";
	public static final String PRODUCT_GATHER = "5";

	// 产品的唯一编码FItemId.金蝶那边就是integer
	@Id
	@Column(name = "PRODUCTID", unique = true, nullable = false)
	private Integer productId;
	// 编码1.判断是ZW还是露芯的产品
	@Column(name = "PBRAND")
	private String pBrand;
	@Column(name = "PRODUCTCODE")
	private String productCode;
	// 产品的名字 FName
	@Column(name = "NAME")
	private String name;
	// 产品的条形码FBarCode
	@Column(name = "BARCODE")
	private String barCode;
	// 心怡物流编码 -F_112
	@Column(name = "LOGISTICSCODE")
	private String logisticsCode;
	// 零售价
	@Column(name = "RETAILPRICE")
	private Double retailPrice;
	// 会员价 f_102
	@Column(name = "MEMBERPRICE")
	private Double memberPrice;
	// 物料价格f_103
	@Column(name = "MATERIALPRICE")
	private Double materialPrice;
	// 工厂 f_101
	@Column(name = "MANUFACTURER")
	private String manufacturer;
	// 单个商品的规格 FModel
	@Column(name = "SPECIFICATIONS")
	private String specifications;
	// 体积
	@Column(name = "SIZE")
	private Double size;
	// 长
	@Column(name = "LENGTH")
	private Double length;
	// 宽
	@Column(name = "WIDTH")
	private Double width;
	// 高
	@Column(name = "HEIGHT")
	private Double height;
	// 商品的最大规格F_111
	@Column(name = "BOXNUM")
	private String boxNum;
	// 商品类型1()
	@Column(name = "TYPEID")
	private String typeId;
	// 商品类型1名称
	@Column(name = "TYPENAME")
	private String typeName;
	// 商品类型2
	@Column(name = "FITEMCLASSID")
	private String fitemClassId;
	// 商品类型名称2
	@Column(name = "FITEMCLASSNAME")
	private String fitemClassName;
	// 产品的身体部位分类id
	@Column(name = "bodyTypeId")
	private String bodyTypeId;
	// 产品的身体部位分类名称
	@Column(name = "bodyTypeName")
	private String bodyTypeName;
	// 每件商品图片的路径
	@Column(name = "IMAGEURL")
	private String imageURL;
	// 自定义的商品的类型的set集合
	@Column(name = "TYPE")
	@Type(type = "com.zeusas.core.entity.StringSetType")
	private Set<String> type;
	// 商品的描述
	@Column(name = "DESCRIPTION")
	private String description;
	// 商品的状态:
	@Column(name = "STATUS")
	private Integer status;
	
	@Column(name = "AVALIBLE")
	private Boolean avalible;
	// 序列id
	@Column(name = "SEQID")
	private Integer seqId;
	/** 金蝶下发状态 是 或者否*/
	@Column(name = "ISSUED")
	private String issued;
	// 最后更新时间
	@Column(name = "LASTUPDATE")
	private Long lastUpdate;
	
	@Transient
	final ThreadLocal<Boolean > actFlag = new ThreadLocal<>();
	@Transient
	final ThreadLocal<Boolean > actItself = new ThreadLocal<>();
	@Transient
	private int minUnit = 1;
	/** 商品的销售数量 */
	@Transient
	private Integer qty;
	@Transient
	private final Map<Integer, Integer> inv;
	/** 是否为打欠产品 */
	@Transient
	final ThreadLocal<Integer > prePro = new ThreadLocal<>();
	
	public Product() {
		actFlag.set(Boolean.FALSE);
		actItself.set(Boolean.FALSE);
		prePro.set(Integer.valueOf(-1));// 正常商品
		
		inv = new HashMap<Integer,Integer>(){
			private static final long serialVersionUID = 1L;
			@Override
			public Integer get(Object key) {
				return containsKey(key)?super.get(key):Integer.valueOf(0);
			}
		};
	}
	
	public int getMinUnit() {
		return minUnit;
	}

	public void setMinUnit(Integer minUnit) {
		if(minUnit!=null){
			this.minUnit = minUnit;
		}
	}

	public boolean getActItself() {
		Boolean flag = actItself.get();
		return flag == null ? false : actFlag.get();
	}

	public void setActItself(boolean b) {
		actItself.set(b);
	}
	
	public Map<Integer, Integer> getInv() {
		return inv;
	}

	public void addInv(Integer invNo, Integer val) {
		if (invNo != null && val != null) {
			inv.put(invNo, val);
		}
	}

	public void setInv(Map<Integer, Integer> inv) {
		if (inv != null) {
			this.inv.putAll(inv);
		}
	}

	public Integer getQty() {
		return qty == null ? 0 : qty;
	}

	public void setQty(Integer qty) {
		this.qty = qty;
	}

	public Boolean isAvalible() {
		if (avalible == null){
			return Boolean.TRUE;
		}
		return avalible;
	}
	
	public Boolean getAvalible() {
		return isAvalible();
	}

	public void setAvalible(Boolean avalible) {
		if (avalible != null) {
			this.avalible = avalible;
		}
	}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public String getpBrand() {
		return pBrand;
	}

	public void setpBrand(String pBrand) {
		this.pBrand = pBrand == null ? "" : pBrand.intern();
	}

	public String getProductCode() {
		return this.productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode == null ? "" : productCode.intern();
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name == null ? "" : name.intern();
	}

	public String getBarCode() {
		return this.barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode == null ? null : barCode.intern();
	}

	public String getLogisticsCode() {
		return this.logisticsCode;
	}

	public void setLogisticsCode(String lc) {
		this.logisticsCode = lc == null ? null : lc.intern();
	}

	public Double getRetailPrice() {
		return this.retailPrice;
	}

	public void setRetailPrice(Double retailPrice) {
		this.retailPrice = retailPrice;
	}

	public Double getMemberPrice() {
		return this.memberPrice;
	}

	public void setMemberPrice(Double memberPrice) {
		this.memberPrice = memberPrice;
	}

	public Double getMaterialPrice() {
		return this.materialPrice;
	}

	public void setMaterialPrice(Double materialPrice) {
		this.materialPrice = materialPrice;
	}

	public String getManufacturer() {
		return this.manufacturer;
	}

	public void setManufacturer(String m) {
		this.manufacturer = m == null ? null : m.intern();
	}

	public String getSpecifications() {
		return this.specifications;
	}

	public void setSpecifications(String spec) {
		this.specifications = spec == null ? null : spec.intern();
	}

	public Double getSize() {
		return this.size;
	}

	public void setSize(Double size) {
		this.size = size;
	}

	public Double getLength() {
		return this.length;
	}

	public void setLength(Double length) {
		this.length = length;
	}

	public Double getWidth() {
		return this.width;
	}

	public void setWidth(Double width) {
		this.width = width;
	}

	public Double getHeight() {
		return this.height;
	}

	public void setHeight(Double height) {
		this.height = height;
	}

	public String getBoxNum() {
		return this.boxNum;
	}

	public void setBoxNum(String boxNum) {
		this.boxNum = boxNum == null ? null : boxNum;
	}

	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName == null ? null : typeName;
	}

	/**
	 * 取得系列ID，如山茶花等
	 * 
	 * @return 系列ID
	 */
	public String getFitemClassId() {
		return this.fitemClassId;
	}

	public void setFitemClassId(String fitemClassId) {
		this.fitemClassId = fitemClassId == null ? null : fitemClassId;
	}

	public String getFitemClassName() {
		return this.fitemClassName;
	}

	public void setFitemClassName(String fitemClassName) {
		this.fitemClassName = fitemClassName;
	}

	public String getImageURL() {
		return imageURL;
	}

	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Long getLastUpdate() {
		return this.lastUpdate;
	}

	public void setLastUpdate(Long lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public Set<String> getType() {
		return type;
	}

	public void setType(Set<String> type) {
		this.type = type;
	}

	public Integer getSeqId() {
		return seqId;
	}

	public void setSeqId(Integer seqId) {
		this.seqId = seqId;
	}

	public String getBodyTypeId() {
		return bodyTypeId;
	}

	public void setBodyTypeId(String bodyTypeId) {
		this.bodyTypeId = bodyTypeId;
	}

	public String getBodyTypeName() {
		return bodyTypeName;
	}

	public void setBodyTypeName(String bodyTypeName) {
		this.bodyTypeName = bodyTypeName;
	}
	
	public boolean getActFlag() {
		return isActFlag();
	}
	
	public boolean isActFlag() {
		Boolean flag = actFlag.get();
		return flag == null ? false : flag;
	}

	public void setActFlag(boolean actFlag) {
		this.actFlag.set(actFlag);
	}
	
	public Integer getPrePro() {
		return isPrePro();
	}
	
	public Integer isPrePro() {
		Integer flag = prePro.get();
		return flag == null ? Integer.valueOf(-1) : flag;
	}

	public void setPrePro(Integer prePro) {
		this.prePro.set(prePro);
	}
	

	public int hashCode() {
		return productId == null ? 0 : productId.hashCode();
	}

	public String getIssued() {
		return issued;
	}

	public void setIssued(String issued) {
		this.issued = issued;
	}

	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!obj.getClass().isInstance(this)) {
			return false;
		}
		Product other = (Product) obj;
		if (!Objects.equals(this.productId, other.productId)) {
			return false;
		}
		if (!Objects.equals(this.pBrand, other.pBrand)) {
			return false;
		}
		if (!Objects.equals(this.productCode, other.productCode)) {
			return false;
		}
		if (!Objects.equals(this.name, other.name)) {
			return false;
		}
		if (!Objects.equals(this.barCode, other.barCode)) {
			return false;
		}
		if (!Objects.equals(this.logisticsCode, other.logisticsCode)) {
			return false;
		}
		if (!Objects.equals(this.retailPrice, other.retailPrice)) {
			return false;
		}
		if (!Objects.equals(this.memberPrice, other.memberPrice)) {
			return false;
		}
		// 同步时候小数位的问题
		if (!Objects.equals(this.materialPrice, other.materialPrice)) {
			return false;
		}
		if (!Objects.equals(this.manufacturer, other.manufacturer)) {
			return false;
		}
		if (!Objects.equals(this.specifications, other.specifications)) {
			return false;
		}
		if (!Objects.equals(this.size, other.size)) {
			return false;
		}
		if (!Objects.equals(this.typeId, other.typeId)) {
			return false;
		}
		if (!Objects.equals(this.fitemClassId, other.fitemClassId)) {
			return false;
		}
		if (!Objects.equals(this.avalible, other.avalible)) {
			return false;
		}
		if (!Objects.equals(this.issued, other.issued)) {
			return false;
		}
		return Objects.equals(this.status, other.status);// FIXME
	}

	public String toString() {
		return JSON.toJSONString(this);
	}

	public Product clone() {
		try {
			return (Product) super.clone();
		} catch (Exception e) {
			// NOP
		}

		Product p = new Product();
		BeanDup.dup(this, p);
		return p;
	}
}
