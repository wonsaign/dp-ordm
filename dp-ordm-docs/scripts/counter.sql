select t3008.FItemID as OrgId,t3008.F_110 as 'CounterCode',F_109
--,t3008.FNumber,t3008.FName
,F_115 as 'Contact',F_117 as 'Mobile',F_116 as 'Phone',N'中国' as Country
,p.FName as 'Province',c.fName as 'City',F_120 as 'AreaCounty',F_106 as 'AreaPrice'
,F_118 as 'Address',F_118 as Shipping_Address,F_119 as PostCode,F_105 as Channel
--,f_108 as 'CustomerID'
,t3007.FName as 'Type',s.FDeleted as Status,getdate() as LastUpdate
from [AIS20130314203706].[dbo].[t_Item_3008] t3008
JOIN [AIS20130314203706].[dbo].[t_Item] t3007 on t3008.F_101 = t3007.FItemID --门店类型
JOIN [AIS20130314203706].[dbo].[t_Item] s on t3008.FItemID=s.FItemID --状态
JOIN [AIS20130314203706].[dbo].[t_Item] p on t3008.F_103 = p.FItemID and p.FItemClassID='3009' --省份
JOIN [AIS20130314203706].[dbo].[t_Item] c on t3008.F_104 = c.FItemID and c.FItemClassID='3010' --城市
where s.FDeleted=0
and t3008.FName not like '%撤%'
