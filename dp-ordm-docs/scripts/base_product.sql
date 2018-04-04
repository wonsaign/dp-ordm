delete OPENQUERY(WMS_MYSQLNEW,'select * from dp_order_beta.base_product')

INSERT INTO OPENQUERY(WMS_MYSQLNEW,'select productid,pbrand,productcode,name,barcode,logisticscode,retailprice,memberprice,materialprice,manufacturer,specifications,size,length,width,height,boxnum,typeid,typename,bodytypeid,bodytypename,fitemclassid,fitemclassname,status,lastupdate from dp_order_beta.base_product')

select distinct t.FItemID as ProductId,t0.FNumber as PBrand,t.FNumber as productcode,t.FName as Name
,coalesce(t1.FBarCode,'无') as BarCode,t.F_112 as LogisticsCode,t.F_108 as RetailPrice,t.F_102 as MemberPrice
,t.F_103 as MaterialPrice,t.F_101 as Manufacturer,t.FModel as Specifications,t.FSize as Size
,t.FLength as Length,t.FWidth as Width,t.FHeight as Height,t.f_111 as BoxNum
,t4.[FItemID] as TypeID,t4.[FName] as TypeName
,t3.fitemid as bodytypeid,t3.fname as bodytypename
,t2.[FItemID] as FItemClassID,t2.[FName] as FItemClassName
,t.FDeleted as Status,cast(GETDATE() as bigint) as LastUpdateTime-- ,inv.fqty,s.FNumber
from [AIS20130314203706].[dbo].[t_ICItem] t --商品主表
join [AIS20130314203706].[dbo].[t_Item] t0 on t.F_105 = t0.FItemID and t0.FItemClassID = 3002 --商品品牌
left join t_BarCode t1 on t1.FItemID = t.FItemID and t1.FTypeID = 4 --条形码
left join t_item t4 on t.f_113=t4.fitemid and  t4.FItemClassID=3016 --商品类型
left join t_item t3 on t.F_135 = t3.fitemid  and t3.FItemClassID = 3027 and t3.FLevel = 1 and t3.FDetail = 1 --部位分类
left join t_item t2 on t.FNumber like t2.FNumber + '.%' and t2.FItemClassID = 4 and t2.FLevel = 2 and t2.FDetail = 0 --系列分类
join [AIS20130314203706].[dbo].[ICInventory] inv on inv.FItemID = t.FItemID
join [AIS20130314203706].[dbo].[t_Stock] s on inv.FStockID = s.FItemID
where s.FNumber in ('4.01','4.03','4.05') --根据指定仓库库存判断 '3.01','3.03','3.05',
and t.F_112 is not null --物流编码不能为null
and inv.FQty >100 --数量大于0
--and t.F_106 = '是'
and t.FDeleted = 0

--union all

--select distinct t.FItemID as ProductId,t0.FNumber as PBrand,t.FNumber as productcode,t.FName as Name
--,coalesce(t1.FBarCode,'无') as BarCode,t.F_112 as LogisticsCode,t.F_108 as RetailPrice,t.F_102 as MemberPrice
--,t.F_103 as MaterialPrice,t.F_101 as Manufacturer,t.FModel as Specifications,t.FSize as Size
--,t.FLength as Length,t.FWidth as Width,t.FHeight as Height,t.f_111 as BoxNum
--,t4.[FItemID] as TypeID,t4.[FName] as TypeName
--,t3.fitemid as bodytypeid,t3.fname as bodytypename
--,t2.[FItemID] as FItemClassID,t2.[FName] as FItemClassName
--,t.FDeleted as Status,cast(GETDATE() as bigint) as LastUpdateTime --,inv.FQty
--from [AIS20130314203706].[dbo].[t_ICItem] t --商品主表
--left join [AIS20130314203706].[dbo].[t_Item] t0 on t.F_105 = t0.FItemID and t0.FItemClassID = 3002 --商品品牌
--left join t_BarCode t1 on t1.FItemID = t.FItemID and t1.FTypeID = 4 --条形码
--left join t_item t4 on t.f_113=t4.fitemid and  t4.FItemClassID=3016 --商品类型
--left join t_item t3 on t.F_135 = t3.fitemid  and t3.FItemClassID = 3027 and t3.FLevel = 1 and t3.FDetail = 1 --部位分类
--left join t_item t2 on t.FNumber like t2.FNumber + '.%' and t2.FItemClassID = 4 and t2.FLevel = 2 and t2.FDetail = 0 --系列分类
--join [AIS20130314203706].[dbo].[ICInventory] inv on inv.FItemID = t.FItemID
--join [AIS20130314203706].[dbo].[t_Stock] s on inv.FStockID = s.FItemID
--where  t.fnumber like 'W.%' 
--and t.FDeleted = 0
--and t.F_112 is not null
--and s.FNumber in ('3.01','3.03','3.05','4.01','4.03','4.05') --根据指定仓库库存判断
--and inv.FQty >0 --数量大于0




select t.* from 
[AIS20130314203706].[dbo].[t_ICItem] t
--left join [AIS20130314203706].[dbo].[t_Item] b on t.f_139=b.fitemid and b.FItemClassID = 4 and b.FLevel = 2
where t.FNumber like N'Z.LC.523'


select * from [AIS20130314203706].[dbo].[t_Item] b where b.FItemClassID = 4 and b.FLevel = 2

select fitemid,fname from [AIS20130314203706].[dbo].[t_Item] b where b.FItemClassID = 3027

select fitemid,fname from [AIS20130314203706].[dbo].[t_Item] b where b.FItemClassID = 3016

select fitemid,fname from [AIS20130314203706].[dbo].[t_Item] b where b.FItemClassID = 4 and b.FLevel = 2 and b.FDetail = 0

SELECT * FROM T_TableDescription where ftableid=60
SELECT * FROM T_FieldDescription WHERE FDescription LIKE '%护肤系列%'
SELECT * FROM T_FieldDescription WHERE FTableID=60

select * from t_ICItemCustom                                                                  