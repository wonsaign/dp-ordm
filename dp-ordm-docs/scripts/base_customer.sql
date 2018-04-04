delete OPENQUERY(WMS_MYSQLNEW,'select * from base_customer')
INSERT INTO OPENQUERY(WMS_MYSQLNEW,'select customerid,parentid,customername,mobile,phone,
contact,customertypeid,customertype,province,city,areacounty,address,postcode,status,lastupdate
 from base_customer')
select a.FItemID 'customerid',a.FparentID as 'parentid',a.fname as 'customername'
,a.FMobilePhone as 'Mobile',a.FPhone as 'Phone',a.FContact as 'Contact'
,ogt.FItemID as 'CustomerTypeID',ogt.FName as 'CustomerType'
,b.FName as 'Province',c.FName as 'City',a.F_106 as 'AreaCounty'
,a.FAddress as 'Address',FPostalCode as PostCode,s.FDeleted as Status,cast(getdate() as bigint) as lastUpdate
 from [AIS20130314203706].[dbo].[t_Organization] a 
join [AIS20130314203706].[dbo].[CRM_Province] b on a.FProvinceID = b.FID  
join [AIS20130314203706].[dbo].[CRM_City] c on a.FCityID = c.FID 
JOIN [AIS20130314203706].[dbo].[t_Item_3017] ogt on a.F_107 = ogt.FItemID
JOIN [AIS20130314203706].[dbo].[t_Item] s on a.FItemID=s.FItemID --状态
where s.FDeleted = 0
and a.FName not like '%撤%' 
and b.FName in ('湖北省','湖南省')
--and ogt.FName not like '%直营%'


select CONVERT(varchar, getdate(), 120 )
select cast(getdate() as bigint) as lastUpdate