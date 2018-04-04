/*
ͬ�����򣺶���ϵͳͬ�������
˵����
	1. δͬ�� 0
	2. ����ͬ�� 2
	3. ��ͬ�� 1
	4. ͬ���쳣 9
AIS20170111150137
select * from OPENQUERY(WMS_MYSQLNEW, 'select * from bus_packagedetail')
���۵��ŵ�id�ֶ� FHeadSelfB0168
BILL_I Bill_WMS_ERP
����bill_i������ʱ������
*/

/**
*
*ֱӪ���۵�����
*
*
**/
use AIS20130314203706
--������  ֻ�������۵� ���ݱ�������ܲ�ֱӪ�ŵ�
UPDATE OPENQUERY (WMS_MYSQLNEW, 'select * from bus_package')
set Status = 0,Message = '',UpdateTime = CONVERT(bigint ,GETDATE(), 120) where Status=9 
and counterid in(
	select fitemid from t_Item_3008
	where FNumber like 'Z.Z%' or FNumber like 'B.Z%')

--�����ݱ���ظ��ļ�¼
update OPENQUERY (WMS_MYSQLNEW, 'select * from bus_package')
set Status = 9,Message = '����:�����ѵ���',UpdateTime = CONVERT(bigint ,GETDATE(), 120)
where id in (select distinct FBillNo from ICStockBill where FBillNo in
					(select * from OPENQUERY (WMS_MYSQLNEW, 'select id from bus_package where Status=0')) )

--ɾ����ʱ������
truncate table Bill_WMS_ERP

--��ʼ����
update OPENQUERY (WMS_MYSQLNEW, 'select * from bus_package')
set Status = 2
where Status = 0
and counterid in(
	select fitemid from t_Item_3008
	where FNumber like 'Z.Z%' or FNumber like 'B.Z%')

--��ȡ���ݵ���ʱ��  �ŵ�����ͻ��ֶ� fcompany:counterid fdcstock:warehousecode
insert into Bill_WMS_ERP(FBillNo_I,FEntryID,FInterID,FCompanyID,FCompany,
FBillNo,FDate,FDepartmentID,FDepartment,FEmployeeID,
FEmployee,FFManagerID,FFManager,FSManagerID,FSManager,
FBillTypeID,FBillType,FSCStockID,FSCStock,FDCStockID,
FDCStock,FBillerID,FNote,FItemID,FItem,
FUnitID,FQty,FPrice,FTaxRate,FTaxPrice,
FDiscount,FDiscounttaxprice,FDiscountTaxAmount,FAmount,FAllAmount,
FTaxAmount,FBatchNo,FRemark,FFlag,FError,FHeadSelfB0170,FHeadSelfB0171)

select 'BXX'+v1.id,v1.linenumber,0,0,v1.counterid,
'BXX'+v1.id,GETDATE(),0,'',0,
'',0,'',0,'',
0,'',0,'',0,
v1.warehousecode,0,'',v1.productid,v1.productcode,
0,v1.productqty,v1.unitprice,0,0,
0,0,0,round(v1.productqty * v1.unitprice,2),0,
0,'','',0,'',v1.orderno,v1.id
from OPENQUERY (WMS_MYSQLNEW, 'select a.id,a.orderno,b.linenumber,a.createtime,
	a.counterid,b.productid,b.productcode,b.productqty,b.unitprice,a.warehousecode
	from bus_package a 
	join bus_packagedetail b on a.id=b.packageid
	where a.status=2') v1
left join t_icitem funit on funit.fitemid = v1.productid --��ȡ��Ʒfunitid��Ϣ
where v1.counterid in(
	select fitemid from t_Item_3008
	where FNumber like 'Z.Z%' or FNumber like 'B.Z%')
and v1.productcode not like 'Z.%'
order by v1.id,v1.linenumber

--�����Ƶ���
update Bill_WMS_ERP
set FBillerID = FUserID
from t_user where FName = 'ֱ��ϵͳ'

--�����ֿ�,������Ʒ����͵���ͷ�ֿ� �Զ���ȡ�ֿ��ָ���Ӳֿ�
--�趨�ŵ�����Ŀͻ��ֿ�
update Bill_WMS_ERP set FDCStockID = 
(case when FDCStock = '3' and FItem like'Z.%' then (select FItemID from t_Stock where fnumber= '3.01')
      when FDCStock = '3' and FItem like'C.%' then (select FItemID from t_Stock where fnumber= '3.03')
      when FDCStock = '3' and FItem like'W.%' then (select FItemID from t_Stock where fnumber= '3.05')
      when FDCStock = '4' and FItem like'Z.%' then (select FItemID from t_Stock where fnumber= '4.01')
      when FDCStock = '4' and FItem like'C.%' then (select FItemID from t_Stock where fnumber= '4.03')
      when FDCStock = '4' and FItem like'W.%' then (select FItemID from t_Stock where fnumber= '4.05')
else 0 end)

--�����ŵ�����Ŀͻ�
update Bill_WMS_ERP 
set FCompanyID = t3008.F_109
from t_Item_3008 t3008
where t3008.FItemID=FCompany

--���¶������� ����
update Bill_WMS_ERP set FHeadSelfB0164=11052

--������Ʒ��λ��Ϣ
update Bill_WMS_ERP
set FUnitID=funit.FUnitID
from t_icitem funit 
where funit.fitemid = Bill_WMS_ERP.fitemid --��ȡ��Ʒfunitid��Ϣ

--�ֿ����շ����� ָ��һ��Ĭ�ϵĲֿ�������
update Bill_WMS_ERP set FFManagerID = 4147,FSManagerID = 4147

--����ҵ��Ա�����ţ����ݿͻ����š�ҵ��Ա
update Bill_WMS_ERP set FEmployeeID = o.Femployee,FDepartmentID = o.Fdepartment
from t_Organization o
where FCompanyID = o.FItemID

--������ID
declare @UserID int
declare @InterID int
declare @BillNo nvarchar(50)

--����Cursor
declare c_data cursor for
select distinct FBillNo_I,FBillerID from Bill_WMS_ERP
order by FBillNo_I

open c_data

while (1=1)
begin
	fetch next from c_data into @BillNo,@UserID
	if @@FETCH_STATUS <> 0
		break
	
	--���FInterID
	execute GetICMaxNum 'ICStockBill',@InterID output,1,@UserID
	
	update Bill_WMS_ERP set FInterID = @InterID where FBillNo_I = @BillNo
end

close c_data
deallocate c_data
--���ɵ�����ϸ
INSERT INTO ICStockBillEntry
(FInterID,FEntryID,FBrNo,FItemID,FAuxPropID,FBatchNo,FQty,FUnitID,Fauxqty,
FSecCoefficient,FSecQty,FAuxPlanPrice,FPlanAmount,FAuxPrice,FAmount,
FAuxPriceRef,FAmtRef,FNote,FKFDate,FKFPeriod,FPeriodDate,FSCStockID,FSCSPID,FDCStockID,FDCSPID,
FSNListID,FSourceBillNo,FSourceTranType,FSourceInterId,FSourceEntryID,FICMOBillNo,FICMOInterID,
FPPBomEntryID,FOrderBillNo,FOrderInterID,FOrderEntryID,FPlanMode,FMTONo,FChkPassItem,
FContractBillNO,FContractEntryID,FContractInterID,FPlanPrice,FConsignPrice,FConsignAmount,
FQtyMust,FAuxQtyMust,FPrice,FDiscountRate,FDiscountAmount) 
select FInterID,FEntryID,'0',FItemID,0,'',FQty,FUnitID,FQty,
0,0,0,0,0,0,
0,0,'',Null,0,Null,0,Null,FDCStockID,0,
0,'',71,0,0,'',0,
0,'',0,0,14036,'',1058,
'',0,0,0,FPrice,FAllAmount,
0,0,0,FDiscount,FDiscountTaxAmount
from Bill_WMS_ERP
if @@ERROR = 0
begin
	--����Ʒ���Զ����ֶ�
	update d set FEntrySelfB0173 = t3002.FName from ICStockBillEntry d
	join Bill_WMS_ERP h on d.FInterID = h.FInterID and d.FEntryID = h.FEntryID
	join t_ICItem t on d.FItemID = t.FItemID
	join t_Item t3002 on t.F_105 = t3002.FItemID and FItemClassID = 3002
	
	--��������ʽ ERROR
	update d set FEntrySelfB0169 = (select FInterID from t_SubMessage where FTypeID = 10002 and FName = '����')--???
	from ICStockBillEntry d
	join Bill_WMS_ERP h on d.FInterID = h.FInterID and d.FEntryID = h.FEntryID
	
	--�������ۼۣ����۽��
	update d set FEntrySelfB0170 = F_108 * d.FQty
	from ICStockBillEntry d
	join Bill_WMS_ERP h on d.FInterID = h.FInterID and d.FEntryID = h.FEntryID
	join t_ICItem t on d.FItemID = t.FItemID
	
	----������˰���۽�� 2016.05.15  alex �޸�20160823 ERROR
	--update d set FEntrySelfB0171 = FConsignAmount /(a.Fvalueaddrate+1)
	--from ICStockBillEntry d 
	--join Bill_WMS_ERP h on d.FInterID = h.FInterID and d.FEntryID = h.FEntryID
	--join t_ICItem t on d.FItemID = t.FItemID
	--join t_organization a on h.fcompanyid =a.fitemid


	----����˰�� 2016.05.15  alex �޸�20160823 ERROR
	--update d set FEntrySelfB0172 = (FConsignAmount /(a.Fvalueaddrate+1))*a.Fvalueaddrate
	--from ICStockBillEntry d
	--join Bill_WMS_ERP h on d.FInterID = h.FInterID and d.FEntryID = h.FEntryID
	--join t_ICItem t on d.FItemID = t.FItemID
	--join t_organization a on h.fcompanyid =a.fitemid
	
	--���ɵ��ݱ�ͷ FCompany:counterid
	INSERT INTO ICStockBill(FInterID,FSupplyID,FBillNo,FBrNo,FTranType,FCancellation,FStatus,FUpStockWhenSave,
	FHookStatus,FDate,FCheckDate,FFManagerID,FSManagerID,FBillerID,FMultiCheckDate1,FMultiCheckDate2,
	FMultiCheckDate3,FMultiCheckDate4,FMultiCheckDate5,FMultiCheckDate6,FSelTranType,FBrID,FDeptID,
	FEmpID,FRefType,FPrintCount,FVchInterID,FROB,FSaleStyle,FPOStyle,FRelateBrID,FPOOrdBillNo,
	FSettleDate,FManageType,FPOMode,FOrderAffirm,FConsignee,FHeadSelfB0168,FHeadSelfB0164,FHeadSelfB0170,FHeadSelfB0171) 
	select distinct FInterID,FCompanyID,FBillNo,'0',21,0,0,0,
	0,FDate,Null,FFManagerID,FSManagerID,FBillerID,Null,Null,
	Null,Null,Null,Null,71,0,FDepartmentID,
	FEmployeeID,0,0,0,1,101,Null,0,'',
	FDate,0,0,0,0,FCompany,FHeadSelfB0164,FHeadSelfB0170,FHeadSelfB0171
	from Bill_WMS_ERP
	if @@ERROR = 0
	begin
		--������ʱ��
		update ICStockBill set FHeadSelfB0161 = convert(nvarchar,GETDATE(),120)
		from Bill_WMS_ERP
		where ICStockBill.FInterID = Bill_WMS_ERP.FInterID
	end
end


/**
*
*ֱӪ����������
*
*
**/

use AIS20130314203706
--ɾ����ʱ������
truncate table Bill_WMS_ERP

--��ʼ���� ������¼

--��ȡ���ݵ���ʱ��
insert into Bill_WMS_ERP(FBillNo_I,FEntryID,FInterID,FCompanyID,FCompany,
FBillNo,FDate,FDepartmentID,FDepartment,FEmployeeID,
FEmployee,FFManagerID,FFManager,FSManagerID,FSManager,
FBillTypeID,FBillType,FSCStockID,FSCStock,FDCStockID,
FDCStock,FBillerID,FNote,FItemID,FItem,
FUnitID,FQty,FPrice,FTaxRate,FTaxPrice,
FDiscount,FDiscounttaxprice,FDiscountTaxAmount,FAmount,FAllAmount,
FTaxAmount,FBatchNo,FRemark,FFlag,FError,FHeadSelfD0134,FHeadSelfD0135)

select 'BDD'+v1.id,v1.linenumber,0,0,v1.counterid,
'BDD'+v1.id,GETDATE(),0,'',0,
'',0,'',0,'',
0,'',0,v1.warehousecode,0,
'',0,'',v1.productid,v1.productcode,
0,v1.productqty,v1.unitprice,0,0,
0,0,0,round(v1.productqty * v1.unitprice,2),0,
0,'','',0,'',v1.orderno,v1.id
from OPENQUERY (WMS_MYSQLNEW, 'select a.id,a.orderno,b.linenumber,a.createtime,
	a.counterid,b.productid,b.productcode,b.productqty,b.unitprice,a.warehousecode
	from bus_package a 
	join bus_packagedetail b on a.id=b.packageid
	where a.status=2') v1
left join t_icitem funit on funit.fitemid = v1.productid --��ȡ��Ʒfunitid��Ϣ
where v1.counterid in(
	select fitemid from t_Item_3008
	where FNumber like 'Z.Z%' or FNumber like 'B.Z%')
	and v1.productcode like 'Z.%'
order by v1.id,v1.linenumber

--�����Ƶ���
update Bill_WMS_ERP
set FBillerID = FUserID
from t_user where FName = 'ֱ��ϵͳ'

--�����ֿ�,������Ʒ����͵���ͷ�ֿ� �Զ���ȡ�ֿ��ָ���Ӳֿ�
update Bill_WMS_ERP set FSCStockID = 
(case when FSCStock = '3' and FItem like'Z.%' then (select FItemID from t_Stock where fnumber= '3.01')
      when FSCStock = '3' and FItem like'C.%' then (select FItemID from t_Stock where fnumber= '3.03')
      when FSCStock = '3' and FItem like'W.%' then (select FItemID from t_Stock where fnumber= '3.05')
      when FSCStock = '4' and FItem like'Z.%' then (select FItemID from t_Stock where fnumber= '4.01')
      when FSCStock = '4' and FItem like'C.%' then (select FItemID from t_Stock where fnumber= '4.03')
      when FSCStock = '4' and FItem like'W.%' then (select FItemID from t_Stock where fnumber= '4.05')
else 0 end)

--���µ���ֿ⣬�ͻ� ���ݲֿ��Ӧ��̨��
update Bill_WMS_ERP set FDCStockID = t3008.F_108,	--��Ӧ�ֿ�
FCompanyID = t3008.F_109 --��Ӧ�ͻ�
from t_Item_3008 t3008
where t3008.FItemID=FCompany

--������Ʒ��λ��Ϣ
update Bill_WMS_ERP
set FUnitID=funit.FUnitID
from t_icitem funit 
where funit.fitemid = Bill_WMS_ERP.fitemid --��ȡ��Ʒfunitid��Ϣ

--���²ֿ������ˣ�ʹ�õ���ֿ����Ա
update Bill_WMS_ERP set FFManagerID = s.FEmpID,FSManagerID = s.FEmpID
from t_Stock s where Bill_WMS_ERP.FDCStockID = s.FItemID

--����ҵ��Ա�����ţ����ݿͻ����š�ҵ��Ա
update Bill_WMS_ERP set FEmployeeID = o.Femployee,FDepartmentID = o.Fdepartment
from t_Organization o
where FCompanyID = o.FItemID

--���¶�������
update Bill_WMS_ERP set FRemark = 11052

--������ID
declare @fbillno nvarchar(100)
declare @FInterID int
declare @billerid int

--����Cursor
DECLARE c_billno cursor FOR
select distinct FBillNo_I,FBillerID from Bill_WMS_ERP order by FBillNo_I

OPEN c_billno

WHILE (1=1)
BEGIN
	FETCH NEXT FROM c_billno INTO @fbillno,@billerid
	if @@FETCH_STATUS <> 0
		break
	
	--���FInterID
	execute GetICMaxNum @TableName = 'ICStockBill',@FInterID = @FInterID output,@Increment = 1,@UserID = @billerid
	--����FInterID
	update Bill_WMS_ERP set finterid = @FInterID
	where FBillNo_I = @fbillno
END

CLOSE c_billno
DEALLOCATE c_billno

--���ɵ�������ϸ
INSERT INTO ICStockBillEntry
(FInterID,FEntryID,FBrNo,FItemID,FAuxPropID,FBatchNo,FQty,FUnitID,Fauxqty,
FSecCoefficient,FSecQty,FAuxPlanPrice,FPlanAmount,Fauxprice,Famount,
FAuxPriceRef,FAmtRef,FNote,FKFDate,FKFPeriod,FPeriodDate,FSCStockID,FSCSPID,FDCStockID,FDCSPID,
FSNListID,FSourceBillNo,FSourceTranType,FSourceInterId,FSourceEntryID,FICMOBillNo,FICMOInterID,
FPPBomEntryID,FOrderBillNo,FOrderInterID,FOrderEntryID,FPlanMode,FMTONo,FChkPassItem,
FContractBillNO,FContractEntryID,FContractInterID,FPlanPrice,FEntrySelfD0158) 
select FInterID,FEntryID,'0',FItemID,0,'',FQty,FUnitID,FQty,
0,0,0,0,0,0,
0,0,FRemark,Null,0,Null,FSCStockID,0,FDCStockID,0,
0,'',0,0,0,'',0,
0,'',0,0,14036,'',1058,
'',0,0,0,FQty
from Bill_WMS_ERP
if @@ERROR = 0
begin
	--����Ʒ���Զ����ֶ�
	update d set FEntrySelfD0162 = t3002.FName from ICStockBillEntry d
	join Bill_WMS_ERP h on d.FInterID = h.FInterID and d.FEntryID = h.FEntryID
	join t_ICItem t on d.FItemID = t.FItemID
	join t_Item t3002 on t.F_105 = t3002.FItemID and FItemClassID = 3002
	
	--��������ʽ
	update d set FEntrySelfD0157 = (select FInterID from t_SubMessage where FTypeID = 10002 and FName = '����')
	from ICStockBillEntry d
	join Bill_WMS_ERP h on d.FInterID = h.FInterID and d.FEntryID = h.FEntryID

	--�������ۼۣ����۽��
	update d set FEntrySelfD0156 = F_108 * d.FQty
	from ICStockBillEntry d
	join Bill_WMS_ERP h on d.FInterID = h.FInterID and d.FEntryID = h.FEntryID
	join t_ICItem t on d.FItemID = t.FItemID
	
	--2016-03-16 �����Ա���
	--update d set FEntrySelfD0170 = F_102 * d.FQty
	--from ICStockBillEntry d
	--join t_ICItem t on d.FItemID = t.FItemID

	--���ɵ�������ͷ
	INSERT INTO ICStockBill(FInterID,FBillNo,FBrNo,FTranType,FCancellation,FStatus,FUpStockWhenSave,
	FHookStatus,FDate,FCheckDate,FFManagerID,FSManagerID,FBillerID,FMultiCheckDate1,FMultiCheckDate2,
	FMultiCheckDate3,FMultiCheckDate4,FMultiCheckDate5,FMultiCheckDate6,FSelTranType,FBrID,FDeptID,
	FEmpID,FRefType,FPrintCount,FVchInterID,FHeadSelfD0134,FHeadSelfD0135)
	select distinct FInterID,FBillNo,'0',41,0,0,0,
	0,FDate,Null,FFManagerID,FSManagerID,FBillerID,Null,Null,
	Null,Null,Null,Null,0,0,FDepartmentID,
	FEmployeeID,12561,0,0,FHeadSelfD0134,FHeadSelfD0135
	from Bill_WMS_ERP
	--��ϸ����ɹ�����ͷ����ʧ�ܵ�����ֹ�����
	if @@ERROR = 0
	begin
		--������ʱ��
		update ICStockBill set FHeadSelfD0142 = convert(nvarchar,GETDATE(),120)
		from Bill_WMS_ERP
		where ICStockBill.FInterID = Bill_WMS_ERP.FInterID
		
		--����������
		update ICStockBill set FHeadSelfD0145 = Bill_WMS_ERP.FRemark
		from Bill_WMS_ERP
		where ICStockBill.FInterID = Bill_WMS_ERP.FInterID
		
		--���������ʽ
		update d set FHeadSelfD0140 = (select FItemID from t_Item where FItemClassID = 3011 and FName = '��������')
		from ICStockBill d
		join Bill_WMS_ERP h on d.FInterID = h.FInterID
		
		--�������
		UPDATE OPENQUERY (WMS_MYSQLNEW, 'select * from bus_package')
		set status=1,updatetime=CONVERT(bigint ,GETDATE(), 120)
		where status=2
		
		UPDATE OPENQUERY (WMS_MYSQLNEW, 'select * from bus_order')
		set orderstatus=6
		where orderstatus=5
		
	end
end
