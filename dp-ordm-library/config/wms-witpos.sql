--物流到店务通业务单据
--1.所有商品均同步到购物车
--2.赠品只获取店务通存在的赠品，不存在的过滤
--3.物料全部不同步到店务通

--单据头
USE [ERP_WITPOS]

CREATE TABLE [dbo].[I_ERP_Invoices](
	[Brand] [varchar](3) NOT NULL,
	[ERPTicketCode] [varchar](40) NOT NULL,
	[TotalQuantity] [int] NULL,
	[TotalAmount] [decimal](12, 2) NULL,
	[Direction] [int] NOT NULL,
	[CounterCode] [nvarchar](30) NOT NULL,
	[Dealer] [nvarchar](20) NULL,
	[GenerateTime] [datetime] NOT NULL,
	[DisCode] [varchar](40) NOT NULL,
	[PreTicketCode] [varchar](33) NULL,
	[SynFlag] [char](1) NOT NULL,
	[GetError] [nvarchar](500) NULL,
	[PutTime] [datetime] NOT NULL,
	[BeginGetTime] [datetime] NULL,
	[EndGetTime] [datetime] NULL,
	[c_Catagory] [varchar](8) NULL,
	[ExpectDeliverTime] [datetime] NULL,
 CONSTRAINT [PK_I_ERP_Invoices] PRIMARY KEY CLUSTERED 
(
	[Brand] ASC,
	[ERPTicketCode] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]

-------------------------

--单据明细
USE [ERP_WITPOS]
GO

CREATE TABLE [dbo].[I_ERP_InvoiceDetail](
	[ERPTicketCode] [varchar](40) NOT NULL,
	[Barcode] [varchar](13) NOT NULL,
	[Unitcode] [varchar](20) NOT NULL,
	[Quantity] [int] NULL,
	[Price] [decimal](12, 2) NULL,
	[DisCode] [varchar](40) NULL,
	[SynFlag] [char](1) NOT NULL,
	[PutTime] [datetime] NOT NULL,
	[BeginGetTime] [datetime] NULL,
	[EndGetTime] [datetime] NULL,
 CONSTRAINT [PK_I_ERP_InvoiceDetail] PRIMARY KEY CLUSTERED 
(
	[ERPTicketCode] ASC,
	[Barcode] ASC,
	[Unitcode] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]

-------------------------

--店务通商品表
USE [ERP_WITPOS]
GO

CREATE TABLE [dbo].[I_ERP_Products](
	[Brand] [varchar](3) NOT NULL,
	[Barcode] [varchar](20) NOT NULL,
	[Unitcode] [varchar](20) NOT NULL,
	[Chinesename] [varchar](40) NULL,
	[Lclasscode] [varchar](4) NOT NULL,
	[Lclassname] [varchar](20) NULL,
	[Bclasscode] [varchar](4) NOT NULL,
	[Bclassname] [varchar](20) NULL,
	[Type] [int] NOT NULL,
	[Price] [decimal](12, 2) NULL,
	[MemPrice] [decimal](12, 2) NOT NULL,
	[Cost] [decimal](12, 2) NULL,
	[Status] [varchar](1) NULL,
	[Modifytime] [datetime] NOT NULL,
	[Gettime] [datetime] NULL,
	[GetStatus] [char](1) NULL,
	[GetError] [nvarchar](500) NULL,
	[K3ProductId] [int] NULL,
	[Manufacturers] [nvarchar](100) NULL,
	[K3ProductCode] [nvarchar](50) NULL,
	[Listingdate] [datetime] NULL,
	[Shelfdate] [datetime] NULL,
	[IsNonExchange] [char](10) NULL,
	[PackingDesign] [varchar](40) NULL,
	[PackingColour] [varchar](40) NULL,
	[Effect] [varchar](40) NULL,
	[Series] [varchar](40) NULL,
	[Parts] [varchar](40) NULL,
	[Age] [varchar](40) NULL,
	[Skin] [varchar](40) NULL,
	[Bases] [varchar](40) NULL,
	[Traits] [varchar](40) NULL,
	[Packagedform] [varchar](40) NULL,
	[NclusionColour] [varchar](40) NULL,
	[Packagematerial] [varchar](40) NULL,
	[NewProduct] [varchar](40) NULL,
 CONSTRAINT [PK_I_ERP_Products] PRIMARY KEY CLUSTERED 
(
	[Brand] ASC,
	[Unitcode] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
