select a.FItemID,a.FparentID,a.fname as Name
,a.FContact as 'Receiver'
, a.FMobilePhone as 'Mobile', a.FPhone as 'Phone',ogt.FName as 'CustomerType'
,b.FName as 'Province',c.FName as 'City',a.F_106 as 'AreaCounty'
,a.FAddress as 'Address',FPostalCode as PostCode,s.FDeleted as Status,getdate() as lastUpdate
 from [AIS20130314203706].[dbo].[t_Organization] a 
join [AIS20130314203706].[dbo].[CRM_Province] b on a.FProvinceID = b.FID 
join [AIS20130314203706].[dbo].[CRM_City] c on a.FCityID = c.FID 
JOIN [AIS20130314203706].[dbo].[t_Item_3017] ogt on a.F_107 = ogt.FItemID
JOIN [AIS20130314203706].[dbo].[t_Item] s on a.FItemID=s.FItemID --状态
where s.FDeleted = 0
and a.FName not like '%撤%'
and ogt.FName not like '%直营%'
and ogt.FName not like '%总部%'


--FitemID,Name,Mobile,Phone,CustomerType,Province,City,AreaCounty,Address,PostCode,Status,lastUpdate
--select * from [AIS20130314203706].[dbo].[t_Organization] --where FparentID=4817
--select * from [AIS20130314203706].[dbo].[t_Item] where FItemID=12400