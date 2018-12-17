USE [RockMaker]
GO
/****** Object:  Table [dbo].[RegionAnnotationType]    Script Date: 01/19/2012 13:13:28 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[RegionAnnotationType](
	[ID] [int] IDENTITY(1,1) NOT NULL,
	[Name] [varchar](100) NOT NULL,
	[TextARGB] [int] NOT NULL,
	[BackgroundARGB] [int] NOT NULL,
	[Icon] [char](1) NOT NULL,
 CONSTRAINT [PK_RegionAnnotationType] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[WellDropScoreGroup]    Script Date: 01/19/2012 13:13:28 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[WellDropScoreGroup](
	[ID] [int] IDENTITY(1,1) NOT NULL,
	[Name] [varchar](50) NOT NULL,
 CONSTRAINT [PK_WellDropScoreGroup] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[ScreenExperiment]    Script Date: 01/19/2012 13:13:28 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[ScreenExperiment](
	[ID] [int] IDENTITY(1,1) NOT NULL,
	[ExperimentID] [int] NOT NULL,
	[Compact] [bit] NOT NULL,
 CONSTRAINT [PK_ScreenExperiment] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[RegionType]    Script Date: 01/19/2012 13:13:28 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[RegionType](
	[ID] [int] IDENTITY(1,1) NOT NULL,
	[Name] [varchar](100) NOT NULL,
 CONSTRAINT [PK_RegionType] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[SearchTarget]    Script Date: 01/19/2012 13:13:28 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[SearchTarget](
	[ID] [int] IDENTITY(1,1) NOT NULL,
	[TargetName] [varchar](50) NOT NULL,
	[TableName] [varchar](128) NOT NULL,
	[TableHints] [varchar](1024) NOT NULL,
	[Joins] [varchar](2048) NOT NULL,
	[TreeNodeField] [varchar](128) NOT NULL,
	[Visible] [bit] NOT NULL,
 CONSTRAINT [PK_SearchTarget] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[SearchConstraintTreeNode]    Script Date: 01/19/2012 13:13:28 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[SearchConstraintTreeNode](
	[ID] [int] IDENTITY(1,1) NOT NULL,
	[RootID] [int] NULL,
	[ParentID] [int] NULL,
	[NodeType] [int] NOT NULL,
 CONSTRAINT [PK_SearchConstraintTreeNode] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[TreeNode]    Script Date: 01/19/2012 13:13:28 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[TreeNode](
	[ID] [int] IDENTITY(1,1) NOT NULL,
	[Type] [varchar](50) NOT NULL,
	[Name] [varchar](64) NOT NULL,
	[ParentID] [int] NULL,
	[SortOrder] [int] NULL,
	[Locked] [bit] NULL,
 CONSTRAINT [PK_Tree] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY],
 CONSTRAINT [IX_ParentIDName] UNIQUE NONCLUSTERED 
(
	[ParentID] ASC,
	[Name] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[SetupTemp]    Script Date: 01/19/2012 13:13:28 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[SetupTemp](
	[ID] [int] IDENTITY(1,1) NOT NULL,
	[Temperature] [int] NOT NULL,
	[IsChilled] [bit] NOT NULL,
 CONSTRAINT [PK_SetupTemp] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Vendor]    Script Date: 01/19/2012 13:13:28 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[Vendor](
	[ID] [int] IDENTITY(1,1) NOT NULL,
	[Name] [varchar](50) NOT NULL,
 CONSTRAINT [PK_Vendor] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY],
 CONSTRAINT [IX_Vendor] UNIQUE NONCLUSTERED 
(
	[Name] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[Users]    Script Date: 01/19/2012 13:13:28 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[Users](
	[ID] [int] IDENTITY(1,1) NOT NULL,
	[Name] [varchar](100) NOT NULL,
	[PasswordHash] [varchar](1024) NOT NULL,
	[PasswordSalt] [varchar](50) NOT NULL,
	[EmailAddress] [varchar](100) NOT NULL,
	[NotificationTypeID] [int] NULL,
 CONSTRAINT [PK_User] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[Layer]    Script Date: 01/19/2012 13:13:28 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[Layer](
	[ID] [bigint] IDENTITY(1,1) NOT NULL,
	[Name] [varchar](250) NOT NULL,
	[X] [int] NOT NULL,
	[Y] [int] NOT NULL,
	[Width] [int] NOT NULL,
	[Height] [int] NOT NULL,
	[TargetDropNumber] [int] NULL,
	[VolumeBasedAddition] [bit] NOT NULL,
	[DispenseManually] [bit] NOT NULL,
	[Override] [bit] NOT NULL,
	[Locked] [bit] NOT NULL,
 CONSTRAINT [PK_Layer] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[IngredientTypeColor]    Script Date: 01/19/2012 13:13:28 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[IngredientTypeColor](
	[ID] [int] IDENTITY(1,1) NOT NULL,
	[GroupID] [int] NOT NULL,
	[ColorToneSequence] [int] NOT NULL,
	[Color] [int] NOT NULL,
 CONSTRAINT [PK_IngredientTypeColor] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Imager]    Script Date: 01/19/2012 13:13:28 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[Imager](
	[ID] [int] IDENTITY(1,1) NOT NULL,
	[RobotID] [varchar](50) NOT NULL,
	[Name] [varchar](50) NOT NULL,
	[Status] [bit] NOT NULL,
 CONSTRAINT [PK_Imager] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[ImagingSchedule]    Script Date: 01/19/2012 13:13:28 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[ImagingSchedule](
	[ID] [int] IDENTITY(1,1) NOT NULL,
	[ExperimentID] [int] NULL,
	[Name] [varchar](50) NOT NULL,
 CONSTRAINT [PK_ImagingSchedule] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[ImageType]    Script Date: 01/19/2012 13:13:28 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[ImageType](
	[ID] [int] IDENTITY(1,1) NOT NULL,
	[LongName] [varchar](100) NOT NULL,
	[ShortName] [varchar](3) NOT NULL,
	[SortOrder] [int] NOT NULL,
	[InRegionView] [bit] NOT NULL,
 CONSTRAINT [PK_ImageType] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[ImageStoreLayout]    Script Date: 01/19/2012 13:13:28 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[ImageStoreLayout](
	[ID] [int] IDENTITY(1,1) NOT NULL,
	[Name] [varchar](100) NOT NULL,
	[ImageCodec] [int] NOT NULL,
 CONSTRAINT [PK_ImageStoreLayout] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[LiquidClass]    Script Date: 01/19/2012 13:13:28 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[LiquidClass](
	[ID] [int] IDENTITY(1,1) NOT NULL,
	[Name] [varchar](128) NOT NULL,
 CONSTRAINT [PK_LiquidClass] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[IncubationTemp]    Script Date: 01/19/2012 13:13:28 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[IncubationTemp](
	[ID] [int] IDENTITY(1,1) NOT NULL,
	[Temperature] [int] NOT NULL,
 CONSTRAINT [PK_IncubationTemp] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[ProteinFormulationIngredientType]    Script Date: 01/19/2012 13:13:28 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[ProteinFormulationIngredientType](
	[ID] [int] IDENTITY(1,1) NOT NULL,
	[Name] [varchar](64) NOT NULL,
 CONSTRAINT [PK_ProteinFormulationType] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[PlateEventType]    Script Date: 01/19/2012 13:13:28 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[PlateEventType](
	[ID] [int] NOT NULL,
	[Name] [varchar](50) NOT NULL,
	[Value] [int] NOT NULL,
 CONSTRAINT [PK_PlateEventType] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[NotificationType]    Script Date: 01/19/2012 13:13:28 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[NotificationType](
	[ID] [int] NOT NULL,
	[Name] [varchar](50) NOT NULL,
 CONSTRAINT [PK_Notification] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[MicrofluidicsImageBatchWellDropRank]    Script Date: 01/19/2012 13:13:28 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[MicrofluidicsImageBatchWellDropRank](
	[ID] [int] IDENTITY(1,1) NOT NULL,
	[ImageBatchWellDropScoreID] [int] NOT NULL,
	[Rank] [float] NOT NULL,
 CONSTRAINT [PK_MicrofluidicsImageBatchWellDropRank] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[PropertyListType]    Script Date: 01/19/2012 13:13:28 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[PropertyListType](
	[ID] [int] IDENTITY(1,1) NOT NULL,
	[Name] [varchar](100) NOT NULL,
 CONSTRAINT [PK_PropertyListType] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[Property]    Script Date: 01/19/2012 13:13:28 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[Property](
	[ID] [int] IDENTITY(1,1) NOT NULL,
	[Name] [varchar](100) NOT NULL,
	[Value] [varchar](1024) NOT NULL,
	[ForeignKeyTable] [varchar](100) NULL,
	[ForeignKeyID] [int] NULL,
 CONSTRAINT [PK_Property] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[BufferCalculationType]    Script Date: 01/19/2012 13:13:28 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[BufferCalculationType](
	[ID] [int] IDENTITY(1,1) NOT NULL,
	[Name] [varchar](50) NOT NULL,
 CONSTRAINT [PK_BufferCalculationType] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[AccessRight]    Script Date: 01/19/2012 13:13:28 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[AccessRight](
	[ID] [int] IDENTITY(1,1) NOT NULL,
	[Name] [varchar](50) NOT NULL,
	[AccessLevel] [int] NOT NULL,
 CONSTRAINT [PK_AccessRight] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[DispensingRobot]    Script Date: 01/19/2012 13:13:28 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[DispensingRobot](
	[ID] [int] IDENTITY(1,1) NOT NULL,
	[Name] [varchar](50) NOT NULL,
 CONSTRAINT [PK_DispensingRobot] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[DatabaseProperty]    Script Date: 01/19/2012 13:13:28 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[DatabaseProperty](
	[Name] [varchar](50) NOT NULL,
	[Value] [varchar](1024) NOT NULL,
 CONSTRAINT [PK_DatabaseProperty] PRIMARY KEY CLUSTERED 
(
	[Name] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[Containers]    Script Date: 01/19/2012 13:13:28 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[Containers](
	[ID] [int] IDENTITY(1,1) NOT NULL,
	[Name] [varchar](100) NOT NULL,
	[NumRows] [int] NOT NULL,
	[NumColumns] [int] NOT NULL,
	[DefaultWellVolume] [float] NOT NULL,
	[DefaultAdditiveVolume] [float] NOT NULL,
	[MaxWellVolume] [float] NOT NULL,
	[MaxNumDrops] [int] NOT NULL,
	[DefaultDropVolume] [float] NOT NULL,
	[MaxDropVolume] [float] NOT NULL,
	[Type] [int] NOT NULL,
 CONSTRAINT [PK_Container] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[Groups]    Script Date: 01/19/2012 13:13:28 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[Groups](
	[ID] [int] IDENTITY(1,1) NOT NULL,
	[Name] [varchar](50) NOT NULL,
 CONSTRAINT [PK_Groups] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[GridVaryAlong]    Script Date: 01/19/2012 13:13:28 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[GridVaryAlong](
	[ID] [int] IDENTITY(1,1) NOT NULL,
	[Name] [varchar](64) NOT NULL,
 CONSTRAINT [PK_GridVaryAlong] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[ExperimentPlateEvent]    Script Date: 01/19/2012 13:13:28 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[ExperimentPlateEvent](
	[ID] [bigint] IDENTITY(1,1) NOT NULL,
	[ExperimentPlateID] [bigint] NOT NULL,
	[DateModified] [datetime] NOT NULL,
 CONSTRAINT [PK_ExperimentPlateEvent] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Experiment]    Script Date: 01/19/2012 13:13:28 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[Experiment](
	[ID] [int] IDENTITY(1,1) NOT NULL,
	[Seeding] [bit] NOT NULL,
	[UserID] [int] NOT NULL,
	[ContainerID] [int] NOT NULL,
	[ImagingScheduleID] [int] NOT NULL,
	[TreeNodeID] [int] NOT NULL,
	[DatePrepared] [datetime] NOT NULL,
	[SetupTempID] [int] NOT NULL,
	[IncubationTempID] [int] NOT NULL,
	[Notes] [varchar](2000) NULL,
	[NotebookPageNum] [varchar](50) NULL,
	[ReducingAgentVolume] [float] NULL,
	[DispensingRobotID] [int] NULL,
	[MaxLotNumber] [int] NULL,
	[PlateRowCount] [int] NOT NULL,
	[PlateColumnCount] [int] NOT NULL,
 CONSTRAINT [PK_Experiment] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[DriverProperty]    Script Date: 01/19/2012 13:13:28 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[DriverProperty](
	[ID] [int] IDENTITY(1,1) NOT NULL,
	[Name] [varchar](100) NOT NULL,
	[Value] [varchar](1024) NOT NULL,
	[DispensingRobotID] [int] NOT NULL,
	[ForeignKeyTable] [varchar](100) NULL,
	[ForeignKeyID] [int] NULL,
 CONSTRAINT [PK_DriverProperty] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[ContainerDrop]    Script Date: 01/19/2012 13:13:28 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[ContainerDrop](
	[ID] [int] IDENTITY(1,1) NOT NULL,
	[ContainerID] [int] NOT NULL,
	[DropNumber] [int] NOT NULL,
 CONSTRAINT [PK_ContainerDrop] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY],
 CONSTRAINT [IX_ContainerDrop] UNIQUE NONCLUSTERED 
(
	[ContainerID] ASC,
	[DropNumber] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Project]    Script Date: 01/19/2012 13:13:28 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[Project](
	[ID] [int] IDENTITY(1,1) NOT NULL,
	[TreeNodeID] [int] NOT NULL,
	[DateCreated] [datetime] NOT NULL,
	[OwnerUserID] [int] NOT NULL,
	[DefaultImagingScheduleID] [int] NOT NULL,
	[Notes] [varchar](2048) NULL,
 CONSTRAINT [PK_Project] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[ProteinFormulation]    Script Date: 01/19/2012 13:13:28 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[ProteinFormulation](
	[ID] [int] IDENTITY(1,1) NOT NULL,
	[TreeNodeID] [int] NOT NULL,
	[ProteinLotID] [varchar](50) NULL,
	[Class] [varchar](50) NULL,
	[DatePrepared] [datetime] NOT NULL,
	[Volume] [float] NULL,
	[StorageTemperature] [float] NULL,
	[ExtinctionCoefficient] [varchar](50) NULL,
	[NotebookPageNo] [varchar](50) NULL,
	[Notes] [varchar](2048) NULL,
 CONSTRAINT [PK_ProteinFormulation] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[PropertyListValue]    Script Date: 01/19/2012 13:13:28 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[PropertyListValue](
	[ID] [int] IDENTITY(1,1) NOT NULL,
	[PropertyListTypeID] [int] NOT NULL,
	[Value] [varchar](1024) NULL,
	[Index] [int] NOT NULL,
	[DisplayValue] [varchar](1024) NULL,
 CONSTRAINT [PK_PropertyListValue] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[Ingredient]    Script Date: 01/19/2012 13:13:28 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[Ingredient](
	[ID] [int] IDENTITY(1,1) NOT NULL,
	[LongName] [varchar](50) NOT NULL,
	[ShortName] [varchar](50) NOT NULL,
	[MaxStockNumber] [int] NOT NULL,
	[DefaultNonBufferIngredientStockID] [int] NULL,
	[DefaultBufferIngredientStockID] [int] NULL,
	[BufferCalculationTypeID] [int] NULL,
	[pKa] [float] NULL,
	[ExportIngredientAliasID] [int] NULL,
 CONSTRAINT [PK_Ingredient] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY],
 CONSTRAINT [IX_Ingredient_LongName] UNIQUE NONCLUSTERED 
(
	[LongName] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY],
 CONSTRAINT [IX_Ingredient_ShortName] UNIQUE NONCLUSTERED 
(
	[ShortName] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[LidStorageType]    Script Date: 01/19/2012 13:13:28 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[LidStorageType](
	[ID] [int] IDENTITY(1,1) NOT NULL,
	[Name] [varchar](50) NOT NULL,
	[NumCols] [int] NOT NULL,
	[NumRows] [int] NOT NULL,
	[NumStack] [int] NOT NULL,
	[DispensingRobotID] [int] NOT NULL,
 CONSTRAINT [PK_LidStorageType] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[ImageStore]    Script Date: 01/19/2012 13:13:28 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[ImageStore](
	[ID] [int] IDENTITY(1,1) NOT NULL,
	[Name] [varchar](100) NOT NULL,
	[ImageStoreLayoutID] [int] NOT NULL,
	[BasePath] [varchar](1024) NOT NULL,
 CONSTRAINT [PK_ImageStore] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[ImagerGroups]    Script Date: 01/19/2012 13:13:28 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[ImagerGroups](
	[ID] [int] IDENTITY(1,1) NOT NULL,
	[GroupID] [int] NOT NULL,
	[ImagerID] [int] NOT NULL,
	[PlateQuota] [int] NOT NULL,
 CONSTRAINT [PK_ImagerGroups] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[ImagingScheduleEntry]    Script Date: 01/19/2012 13:13:28 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[ImagingScheduleEntry](
	[ID] [int] IDENTITY(1,1) NOT NULL,
	[ImagingScheduleID] [int] NOT NULL,
	[Days] [int] NOT NULL,
	[Hours] [int] NOT NULL,
	[Notify] [bit] NOT NULL,
 CONSTRAINT [PK_ImagingScheduleEntry] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[ImageManagement]    Script Date: 01/19/2012 13:13:28 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[ImageManagement](
	[ID] [int] IDENTITY(1,1) NOT NULL,
	[TreeNodeID] [int] NOT NULL,
 CONSTRAINT [PK_ImageManagement] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[GroupUser]    Script Date: 01/19/2012 13:13:28 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[GroupUser](
	[ID] [int] IDENTITY(1,1) NOT NULL,
	[GroupID] [int] NOT NULL,
	[UserID] [int] NOT NULL,
	[PrimaryGroup] [bit] NULL,
 CONSTRAINT [PK_GroupUser] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[IngredientType]    Script Date: 01/19/2012 13:13:28 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[IngredientType](
	[ID] [int] IDENTITY(1,1) NOT NULL,
	[IsBuffer] [bit] NOT NULL,
	[Name] [varchar](50) NOT NULL,
	[IngredientTypeColorID] [int] NULL,
 CONSTRAINT [PK_Type] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY],
 CONSTRAINT [IX_IngredientType_Name_Unique] UNIQUE NONCLUSTERED 
(
	[Name] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[TreeNodeFile]    Script Date: 01/19/2012 13:13:28 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[TreeNodeFile](
	[ID] [int] IDENTITY(1,1) NOT NULL,
	[TreeNodeID] [int] NOT NULL,
	[Data] [varbinary](max) NULL,
	[CreationTime] [datetime] NOT NULL,
 CONSTRAINT [PK_TreeNodeFile] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY],
 CONSTRAINT [UK_TreeNodeFile_TreeNodeID] UNIQUE NONCLUSTERED 
(
	[TreeNodeID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[TreeNodeAccessRight]    Script Date: 01/19/2012 13:13:28 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[TreeNodeAccessRight](
	[ID] [int] IDENTITY(1,1) NOT NULL,
	[AccessRightID] [int] NOT NULL,
	[TreeNodeID] [int] NOT NULL,
	[UserID] [int] NULL,
	[GroupID] [int] NULL,
 CONSTRAINT [PK_TreeNodeAccessRight] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[SearchTargetRelation]    Script Date: 01/19/2012 13:13:28 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[SearchTargetRelation](
	[SearchTargetParentID] [int] NOT NULL,
	[SearchTargetChildID] [int] NOT NULL,
	[ParentField] [varchar](128) NOT NULL,
	[ChildField] [varchar](128) NOT NULL,
 CONSTRAINT [PK_SearchTargetRelations] PRIMARY KEY CLUSTERED 
(
	[SearchTargetParentID] ASC,
	[SearchTargetChildID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[Search]    Script Date: 01/19/2012 13:13:28 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Search](
	[ID] [int] IDENTITY(1,1) NOT NULL,
	[TreeNodeID] [int] NOT NULL,
	[SearchTargetID] [int] NOT NULL,
	[FlattenTargetID] [int] NULL,
	[RootTreeNodeID] [int] NULL,
	[RootSearchConstraintTreeNodeID] [int] NOT NULL,
	[SelectDistinct] [bit] NOT NULL,
 CONSTRAINT [PK_Search] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[SearchField]    Script Date: 01/19/2012 13:13:28 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[SearchField](
	[ID] [int] IDENTITY(1,1) NOT NULL,
	[SearchTargetID] [int] NOT NULL,
	[TableName] [varchar](100) NOT NULL,
	[FieldName] [varchar](50) NOT NULL,
	[DisplayName] [varchar](50) NOT NULL,
	[FieldClass] [varchar](50) NOT NULL,
 CONSTRAINT [PK_SearchField] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[Region]    Script Date: 01/19/2012 13:13:28 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[Region](
	[ID] [bigint] IDENTITY(1,1) NOT NULL,
	[WellDropID] [bigint] NOT NULL,
	[RegionTypeID] [int] NOT NULL,
	[ParentRegionID] [bigint] NULL,
	[RegionName] [varchar](100) NOT NULL,
	[ImagingEnabled] [bit] NOT NULL,
	[XCenterOffset] [float] NULL,
	[YCenterOffset] [float] NULL,
	[ZHeight] [float] NULL,
	[XWidth] [float] NULL,
	[YHeight] [float] NULL,
 CONSTRAINT [PK_Region] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY],
 CONSTRAINT [IX_Region] UNIQUE NONCLUSTERED 
(
	[WellDropID] ASC,
	[RegionName] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[ProteinLayer]    Script Date: 01/19/2012 13:13:28 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[ProteinLayer](
	[ID] [int] IDENTITY(1,1) NOT NULL,
	[ExperimentID] [int] NOT NULL,
	[LayerID] [bigint] NOT NULL,
 CONSTRAINT [PK_ProteinLayer] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[RackType]    Script Date: 01/19/2012 13:13:28 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[RackType](
	[ID] [int] IDENTITY(1,1) NOT NULL,
	[Name] [varchar](50) NOT NULL,
	[NumCols] [int] NOT NULL,
	[NumRows] [int] NOT NULL,
	[TipAccess] [int] NOT NULL,
	[Volume] [float] NOT NULL,
	[DeadVolume] [float] NOT NULL,
	[DispensingRobotID] [int] NOT NULL,
 CONSTRAINT [PK_RackType] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[WellDropScore]    Script Date: 01/19/2012 13:13:28 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[WellDropScore](
	[ID] [int] IDENTITY(1,1) NOT NULL,
	[WellDropScoreGroupID] [int] NOT NULL,
	[Name] [varchar](50) NOT NULL,
	[HotKey] [varchar](2) NOT NULL,
	[OrderNumber] [int] NOT NULL,
	[Color] [int] NOT NULL,
 CONSTRAINT [PK_WellDropScore] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[RegionAnnotation]    Script Date: 01/19/2012 13:13:28 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[RegionAnnotation](
	[ID] [int] IDENTITY(1,1) NOT NULL,
	[RegionID] [bigint] NOT NULL,
	[RegionAnnotationTypeID] [int] NOT NULL,
	[Expanded] [bit] NOT NULL,
	[X] [float] NOT NULL,
	[Y] [float] NOT NULL,
	[Width] [float] NOT NULL,
	[Height] [float] NOT NULL,
	[Font] [varchar](100) NOT NULL,
	[Text] [varchar](4000) NOT NULL,
 CONSTRAINT [PK_RegionAnnotation] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[Rack]    Script Date: 01/19/2012 13:13:28 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[Rack](
	[ID] [int] IDENTITY(1,1) NOT NULL,
	[RackTypeID] [int] NOT NULL,
	[Name] [varchar](50) NOT NULL,
 CONSTRAINT [PK_Rack] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[ProteinLayerProteinFormulation]    Script Date: 01/19/2012 13:13:28 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[ProteinLayerProteinFormulation](
	[ID] [int] IDENTITY(1,1) NOT NULL,
	[ProteinLayerID] [int] NOT NULL,
	[ProteinFormulationID] [int] NOT NULL,
	[StartWellDropVolume] [float] NOT NULL,
	[StopWellDropVolume] [float] NOT NULL,
	[WellDropVolumeVaryAlong] [int] NOT NULL,
	[StartProteinDropVolume] [float] NOT NULL,
	[StopProteinDropVolume] [float] NOT NULL,
	[ProteinDropVolumeVaryAlong] [int] NOT NULL,
 CONSTRAINT [PK_ProteinLayerProteinFormulation] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[RegionScribble]    Script Date: 01/19/2012 13:13:28 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[RegionScribble](
	[ID] [int] IDENTITY(1,1) NOT NULL,
	[RegionID] [bigint] NOT NULL,
	[ARGB] [int] NOT NULL,
	[XStart] [float] NOT NULL,
	[YStart] [float] NOT NULL,
 CONSTRAINT [PK_Scribble] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[RegionRuler]    Script Date: 01/19/2012 13:13:28 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[RegionRuler](
	[ID] [int] IDENTITY(1,1) NOT NULL,
	[RegionID] [bigint] NOT NULL,
	[ARGB] [int] NOT NULL,
	[X1] [float] NOT NULL,
	[Y1] [float] NOT NULL,
	[X2] [float] NOT NULL,
	[Y2] [float] NOT NULL,
 CONSTRAINT [PK_Ruler] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[RegionProperty]    Script Date: 01/19/2012 13:13:28 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[RegionProperty](
	[ID] [bigint] IDENTITY(1,1) NOT NULL,
	[RegionID] [bigint] NOT NULL,
	[Name] [varchar](100) NOT NULL,
	[Value] [varchar](1024) NOT NULL,
 CONSTRAINT [PK_RegionProperty] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[RandomLayer]    Script Date: 01/19/2012 13:13:28 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[RandomLayer](
	[ID] [int] IDENTITY(1,1) NOT NULL,
	[LayerID] [bigint] NOT NULL,
	[ExperimentID] [int] NOT NULL,
	[GridSteps] [int] NOT NULL,
	[WellVolume] [float] NOT NULL,
 CONSTRAINT [PK_RandomLayer] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[ImageManagementAction]    Script Date: 01/19/2012 13:13:28 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[ImageManagementAction](
	[ID] [int] IDENTITY(1,1) NOT NULL,
	[ImageManagementID] [int] NOT NULL,
	[RootSearchConstraintTreeNodeID] [int] NOT NULL,
	[ActionType] [int] NOT NULL,
	[SourceImageStoreID] [int] NOT NULL,
	[TargetImageStoreID] [int] NULL,
	[Verify] [bit] NOT NULL,
 CONSTRAINT [PK_ImageManagementAction] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[ScreenLot]    Script Date: 01/19/2012 13:13:28 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[ScreenLot](
	[ID] [int] IDENTITY(1,1) NOT NULL,
	[ExperimentID] [int] NOT NULL,
	[ContainerID] [int] NOT NULL,
	[TreeNodeID] [int] NOT NULL,
	[LotNumber] [int] NOT NULL,
	[DispenseVolume] [float] NOT NULL,
	[CurrentVolume] [float] NOT NULL,
	[Comment] [varchar](200) NULL,
	[InitialLot] [bit] NOT NULL,
 CONSTRAINT [PK_ScreenLot] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[SearchDisplayField]    Script Date: 01/19/2012 13:13:28 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[SearchDisplayField](
	[ID] [int] IDENTITY(1,1) NOT NULL,
	[SearchFieldID] [int] NOT NULL,
	[SearchID] [int] NOT NULL,
	[DisplayOrder] [int] NOT NULL,
	[ColumnHeader] [varchar](128) NOT NULL,
 CONSTRAINT [PK_SearchDisplayField] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[SearchConstraint]    Script Date: 01/19/2012 13:13:28 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[SearchConstraint](
	[ID] [int] IDENTITY(1,1) NOT NULL,
	[SearchConstraintTreeNodeID] [int] NOT NULL,
	[SearchFieldID] [int] NOT NULL,
	[SearchOperator] [varchar](50) NOT NULL,
	[SearchValue] [varchar](1024) NOT NULL,
 CONSTRAINT [PK_SearchConstraint] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[IngredientStock]    Script Date: 01/19/2012 13:13:28 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[IngredientStock](
	[ID] [int] IDENTITY(1,1) NOT NULL,
	[IngredientID] [int] NOT NULL,
	[StockNumber] [int] NOT NULL,
	[StockConcentration] [float] NOT NULL,
	[LowConcentration] [float] NOT NULL,
	[HighConcentration] [float] NOT NULL,
	[ConcentrationUnits] [varchar](50) NOT NULL,
	[StockPH] [float] NULL,
	[IsBuffer] [bit] NOT NULL,
	[IsPrepared] [bit] NOT NULL,
	[LiquidClassID] [int] NOT NULL,
	[LabelPrinted] [bit] NOT NULL,
	[DispenseOrder] [int] NOT NULL,
	[VendorID] [int] NULL,
	[VendorPartNumber] [varchar](50) NULL,
	[Comments] [varchar](1024) NULL,
 CONSTRAINT [PK_IngredientStock] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[IngredientIngredientType]    Script Date: 01/19/2012 13:13:28 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[IngredientIngredientType](
	[ID] [int] IDENTITY(1,1) NOT NULL,
	[IngredientID] [int] NOT NULL,
	[IngredientTypeID] [int] NOT NULL,
 CONSTRAINT [PK_IngredientType] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY],
 CONSTRAINT [IX_Ingredient_Detail_Type_Unique] UNIQUE NONCLUSTERED 
(
	[ID] ASC,
	[IngredientID] ASC,
	[IngredientTypeID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[IngredientAlias]    Script Date: 01/19/2012 13:13:28 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[IngredientAlias](
	[ID] [int] IDENTITY(1,1) NOT NULL,
	[IngredientID] [int] NOT NULL,
	[Name] [varchar](50) NOT NULL,
 CONSTRAINT [PK_IngredientAlias] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY],
 CONSTRAINT [IX_IngredientAlias_Name_Unique] UNIQUE NONCLUSTERED 
(
	[Name] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[BufferDataPoint]    Script Date: 01/19/2012 13:13:28 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[BufferDataPoint](
	[ID] [int] IDENTITY(1,1) NOT NULL,
	[IngredientID] [int] NOT NULL,
	[pH] [float] NOT NULL,
	[Ratio] [float] NOT NULL,
 CONSTRAINT [PK_BufferDataPoint] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Plate]    Script Date: 01/19/2012 13:13:28 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[Plate](
	[ID] [bigint] IDENTITY(1,1) NOT NULL,
	[PlateNumber] [int] NOT NULL,
	[ExperimentID] [int] NOT NULL,
	[Barcode] [varchar](50) NULL,
	[TreeNodeID] [int] NOT NULL,
	[State] [int] NOT NULL,
	[IsQueued] [bit] NOT NULL,
	[DateDispensed] [datetime] NULL,
	[DispensingRobotID] [int] NULL,
	[CanvasRowPosition] [int] NOT NULL,
	[CanvasColumnPosition] [int] NOT NULL,
 CONSTRAINT [PK_Plate] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[LidStorage]    Script Date: 01/19/2012 13:13:28 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[LidStorage](
	[ID] [int] IDENTITY(1,1) NOT NULL,
	[SiteName] [varchar](50) NOT NULL,
	[LidStorageTypeID] [int] NOT NULL,
 CONSTRAINT [PK_LidStorage] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[ProteinFormulationIngredient]    Script Date: 01/19/2012 13:13:28 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[ProteinFormulationIngredient](
	[ID] [int] IDENTITY(1,1) NOT NULL,
	[ProteinFormulationID] [int] NOT NULL,
	[ProteinFormulationIngredientTypeID] [int] NOT NULL,
	[Name] [varchar](128) NULL,
	[UserID] [varchar](128) NULL,
	[Lot] [varchar](128) NULL,
	[MolecularWeight] [float] NULL,
	[ConcentrationmgmL] [float] NULL,
	[ConcentrationmM] [float] NULL,
 CONSTRAINT [PK_ProteinFormulationIngredient] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[ProteinFormulationAttachment]    Script Date: 01/19/2012 13:13:28 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[ProteinFormulationAttachment](
	[ProteinFormulationID] [int] NOT NULL,
	[Name] [varchar](256) NOT NULL,
 CONSTRAINT [PK_ProteinFormulationAttachment] PRIMARY KEY CLUSTERED 
(
	[ProteinFormulationID] ASC,
	[Name] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[MicrofluidicsExperiment]    Script Date: 01/19/2012 13:13:28 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[MicrofluidicsExperiment](
	[ID] [int] IDENTITY(1,1) NOT NULL,
	[ExperimentID] [int] NOT NULL,
	[FidTime] [int] NULL,
 CONSTRAINT [PK_MicrofluidicsExperiment] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[CASNumber]    Script Date: 01/19/2012 13:13:28 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[CASNumber](
	[ID] [int] IDENTITY(1,1) NOT NULL,
	[Number] [varchar](50) NOT NULL,
	[IngredientID] [int] NOT NULL,
 CONSTRAINT [PK_CASNumber] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[ConditionListLayer]    Script Date: 01/19/2012 13:13:28 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[ConditionListLayer](
	[ID] [int] IDENTITY(1,1) NOT NULL,
	[LayerID] [bigint] NOT NULL,
	[ExperimentID] [int] NOT NULL,
	[WellVolume] [float] NOT NULL,
 CONSTRAINT [PK_ConditionListLayer] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Filter]    Script Date: 01/19/2012 13:13:28 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Filter](
	[ID] [bigint] IDENTITY(1,1) NOT NULL,
	[SearchID] [int] NOT NULL,
	[SearchFieldID] [int] NOT NULL,
 CONSTRAINT [PK_Filter] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Grid]    Script Date: 01/19/2012 13:13:28 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Grid](
	[ID] [int] IDENTITY(1,1) NOT NULL,
	[ExperimentID] [int] NOT NULL,
	[StartWellVolume] [float] NOT NULL,
	[StopWellVolume] [float] NOT NULL,
	[WellVolumeVaryAlong] [int] NOT NULL,
 CONSTRAINT [PK_Grid] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[GridLayer]    Script Date: 01/19/2012 13:13:28 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[GridLayer](
	[ID] [bigint] IDENTITY(1,1) NOT NULL,
	[GridID] [int] NOT NULL,
	[LayerID] [bigint] NOT NULL,
	[SubGridNumber] [int] NOT NULL,
 CONSTRAINT [PK_GridLayer] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[GridIngredient]    Script Date: 01/19/2012 13:13:28 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[GridIngredient](
	[ID] [int] IDENTITY(1,1) NOT NULL,
	[GridID] [int] NOT NULL,
	[IngredientIngredientTypeID] [int] NOT NULL,
	[IngredientStockID] [int] NOT NULL,
	[HighPHIngredientStockID] [int] NULL,
	[StartConcentration] [float] NOT NULL,
	[StopConcentration] [float] NOT NULL,
	[ConcentrationVaryAlong] [int] NOT NULL,
	[StartpH] [float] NULL,
	[StoppH] [float] NULL,
	[pHVaryAlong] [int] NULL,
 CONSTRAINT [PK_GridIngredient] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[FavoriteIngredientStock]    Script Date: 01/19/2012 13:13:28 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[FavoriteIngredientStock](
	[ID] [int] IDENTITY(1,1) NOT NULL,
	[IngredientIngredientTypeID] [int] NOT NULL,
	[IngredientStockID] [int] NOT NULL,
	[UserID] [int] NULL,
 CONSTRAINT [PK_FavoriteIngredientStock] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Condition]    Script Date: 01/19/2012 13:13:28 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Condition](
	[ID] [int] IDENTITY(1,1) NOT NULL,
	[ConditionListLayerID] [int] NOT NULL,
	[ConditionNumber] [int] NOT NULL,
 CONSTRAINT [PK_Condition] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY],
 CONSTRAINT [IX_ConditionIDConditionNumber_Unique] UNIQUE NONCLUSTERED 
(
	[ConditionListLayerID] ASC,
	[ConditionNumber] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[ExperimentPlate]    Script Date: 01/19/2012 13:13:28 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[ExperimentPlate](
	[ID] [bigint] IDENTITY(1,1) NOT NULL,
	[PlateID] [bigint] NOT NULL,
	[ImagingTasksEnabled] [bit] NOT NULL,
	[Notes] [varchar](2000) NULL,
 CONSTRAINT [PK_ExperimentPlate] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[DispenseQueue]    Script Date: 01/19/2012 13:13:28 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[DispenseQueue](
	[ID] [int] IDENTITY(1,1) NOT NULL,
	[PlateID] [bigint] NULL,
	[DateQueued] [datetime] NOT NULL,
	[DispensingRobotID] [int] NULL,
 CONSTRAINT [PK_DispenseQueue] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Deck]    Script Date: 01/19/2012 13:13:28 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[Deck](
	[ID] [int] IDENTITY(1,1) NOT NULL,
	[RackID] [int] NULL,
	[SiteName] [varchar](50) NOT NULL,
	[LidSiteName] [varchar](50) NULL,
	[LidStorageID] [int] NULL,
	[DispensingRobotID] [int] NOT NULL,
 CONSTRAINT [PK_RacksOnDeck] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[PlateEvents]    Script Date: 01/19/2012 13:13:28 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[PlateEvents](
	[ID] [bigint] IDENTITY(1,1) NOT NULL,
	[PlateID] [bigint] NOT NULL,
	[PlateEventTypeID] [int] NOT NULL,
	[EventDate] [datetime] NOT NULL
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[ScreenLotPlate]    Script Date: 01/19/2012 13:13:28 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[ScreenLotPlate](
	[ID] [bigint] IDENTITY(1,1) NOT NULL,
	[ScreenLotID] [int] NOT NULL,
	[PlateID] [bigint] NOT NULL,
	[Migrated] [bit] NOT NULL,
 CONSTRAINT [PK_ScreenLotPlate] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[ScreenLayer]    Script Date: 01/19/2012 13:13:28 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[ScreenLayer](
	[ID] [int] IDENTITY(1,1) NOT NULL,
	[LayerID] [bigint] NOT NULL,
	[ExperimentID] [int] NOT NULL,
	[ScreenLotID] [int] NOT NULL,
	[Volume] [float] NOT NULL,
	[StartingWellNumber] [int] NULL,
 CONSTRAINT [PK_ScreenLayer] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[RegionScribblePoint]    Script Date: 01/19/2012 13:13:28 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[RegionScribblePoint](
	[ID] [bigint] IDENTITY(1,1) NOT NULL,
	[RegionScribbleID] [int] NOT NULL,
	[PointIndex] [int] NOT NULL,
	[XOffset] [float] NOT NULL,
	[YOffset] [float] NOT NULL,
 CONSTRAINT [PK_RegionScribblePoint] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[RandomLayerExcludeList]    Script Date: 01/19/2012 13:13:28 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[RandomLayerExcludeList](
	[ID] [int] IDENTITY(1,1) NOT NULL,
	[RandomLayerID] [int] NOT NULL,
	[Ingredient1ID] [int] NOT NULL,
	[Ingredient2ID] [int] NOT NULL,
 CONSTRAINT [PK_RandomLayerExcludeList] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[RackIngredient]    Script Date: 01/19/2012 13:13:28 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[RackIngredient](
	[ID] [int] IDENTITY(1,1) NOT NULL,
	[RackID] [int] NOT NULL,
	[IngredientStockID] [int] NOT NULL,
	[RackPosition] [int] NOT NULL,
	[Volume] [float] NULL,
	[Barcode] [varchar](50) NULL,
 CONSTRAINT [PK_RackIngredient] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[ProteinFormulationStandardIngredient]    Script Date: 01/19/2012 13:13:28 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[ProteinFormulationStandardIngredient](
	[ID] [int] IDENTITY(1,1) NOT NULL,
	[ProteinFormulationID] [int] NOT NULL,
	[IngredientIngredientTypeID] [int] NOT NULL,
	[IngredientStockID] [int] NOT NULL,
	[HighPHIngredientStockID] [int] NULL,
	[Concentration] [float] NOT NULL,
	[pH] [float] NULL,
 CONSTRAINT [PK_ProteinFormulationStandardIngredient] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[RandomLayerIngredientGroup]    Script Date: 01/19/2012 13:13:28 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[RandomLayerIngredientGroup](
	[ID] [int] IDENTITY(1,1) NOT NULL,
	[RandomLayerID] [int] NOT NULL,
	[Name] [varchar](50) NOT NULL,
 CONSTRAINT [PK_RandomLayerIngredientGroup] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY],
 CONSTRAINT [IX_RandomLayer_GroupName_Unique] UNIQUE NONCLUSTERED 
(
	[RandomLayerID] ASC,
	[Name] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[Well]    Script Date: 01/19/2012 13:13:28 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[Well](
	[ID] [bigint] IDENTITY(1,1) NOT NULL,
	[PlateID] [bigint] NOT NULL,
	[WellNumber] [int] NOT NULL,
	[RowLetter] [varchar](2) NOT NULL,
	[ColumnNumber] [int] NOT NULL,
	[Volume] [float] NULL,
	[WaterVolume] [float] NULL,
	[CurrentVolume] [float] NULL,
 CONSTRAINT [PK_Well] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY],
 CONSTRAINT [IX_Well_ExperimentPlateID_WellNumber] UNIQUE NONCLUSTERED 
(
	[PlateID] ASC,
	[WellNumber] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[WellLayer]    Script Date: 01/19/2012 13:13:28 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[WellLayer](
	[ID] [bigint] IDENTITY(1,1) NOT NULL,
	[WellID] [bigint] NOT NULL,
	[LayerID] [bigint] NOT NULL,
	[Volume] [float] NOT NULL,
	[WaterVolume] [float] NOT NULL,
	[SourceWellID] [bigint] NULL,
	[WaterSource] [varchar](50) NULL,
	[SourceWellConditionNumber] [varchar](50) NULL,
 CONSTRAINT [PK_WellLayer] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[WellDrop]    Script Date: 01/19/2012 13:13:28 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[WellDrop](
	[ID] [bigint] IDENTITY(1,1) NOT NULL,
	[WellID] [bigint] NOT NULL,
	[DropNumber] [int] NOT NULL,
	[ProteinFormulationID] [int] NULL,
	[DropWellVolume] [float] NOT NULL,
	[DropProteinVolume] [float] NOT NULL,
	[Volume] [float] NULL,
	[WaterVolume] [float] NULL,
 CONSTRAINT [PK_WellDrop] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY],
 CONSTRAINT [IX_WellDrop] UNIQUE NONCLUSTERED 
(
	[WellID] ASC,
	[DropNumber] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[RandomLayerGroupIngredient]    Script Date: 01/19/2012 13:13:28 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[RandomLayerGroupIngredient](
	[ID] [int] IDENTITY(1,1) NOT NULL,
	[RandomLayerIngredientGroupID] [int] NOT NULL,
	[IngredientStockID] [int] NOT NULL,
	[HighPHIngredientStockID] [int] NULL,
	[IngredientIngredientTypeID] [int] NOT NULL,
	[LowConcentration] [float] NOT NULL,
	[HighConcentration] [float] NOT NULL,
	[LowPH] [float] NULL,
	[HighPH] [float] NULL,
	[Weight] [float] NOT NULL,
 CONSTRAINT [PK_RandomLayerGroupIngredient] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[RandomLayerGenerationList]    Script Date: 01/19/2012 13:13:28 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[RandomLayerGenerationList](
	[ID] [int] IDENTITY(1,1) NOT NULL,
	[RandomLayerID] [int] NOT NULL,
	[RandomLayerIngredientGroupID] [int] NOT NULL,
	[Probability] [float] NOT NULL,
 CONSTRAINT [PK_RandomLayerGenerationList] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[ImagingTask]    Script Date: 01/19/2012 13:13:28 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[ImagingTask](
	[ID] [int] IDENTITY(1,1) NOT NULL,
	[ExperimentPlateID] [bigint] NOT NULL,
	[ImagingScheduleEntryID] [int] NULL,
	[State] [int] NOT NULL,
	[DateToImage] [datetime] NOT NULL,
	[DateImaged] [datetime] NULL,
	[ClearedFromToImage] [bit] NOT NULL,
	[ClearedFromToScore] [bit] NOT NULL,
 CONSTRAINT [PK_ImagingTasks] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[ConditionIngredient]    Script Date: 01/19/2012 13:13:28 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[ConditionIngredient](
	[ID] [int] IDENTITY(1,1) NOT NULL,
	[ConditionID] [int] NOT NULL,
	[IngredientIngredientTypeID] [int] NOT NULL,
	[IngredientStockID] [int] NOT NULL,
	[HighPHIngredientStockID] [int] NULL,
	[Concentration] [float] NOT NULL,
	[pH] [float] NULL,
 CONSTRAINT [PK_ConditionIngredient] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[CaptureProfile]    Script Date: 01/19/2012 13:13:28 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[CaptureProfile](
	[ID] [int] IDENTITY(1,1) NOT NULL,
	[Name] [varchar](100) NOT NULL,
	[ExperimentPlateID] [bigint] NULL,
	[CurrentCaptureProfileVersionID] [int] NULL,
 CONSTRAINT [PK_CaptureProfile] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY],
 CONSTRAINT [IX_CaptureProfile] UNIQUE NONCLUSTERED 
(
	[Name] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[ExperimentCaptureProfileTuningValues]    Script Date: 01/19/2012 13:13:28 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[ExperimentCaptureProfileTuningValues](
	[ID] [int] IDENTITY(1,1) NOT NULL,
	[ExperimentID] [int] NOT NULL,
	[CaptureProfileID] [int] NOT NULL,
	[AutoLevelingOn] [bit] NOT NULL,
	[AutoLevelingLowThreshold] [decimal](4, 2) NOT NULL,
	[AutoLevelingHighThreshold] [decimal](4, 2) NOT NULL,
	[LowerLevelLimit] [int] NOT NULL,
	[UpperLevelLimit] [int] NOT NULL,
	[Brightness] [int] NOT NULL,
	[Contrast] [int] NOT NULL,
	[Gamma] [int] NOT NULL,
 CONSTRAINT [PK_ExperimentCaptureProfileTuningValues] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[ImageBatch]    Script Date: 01/19/2012 13:13:28 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[ImageBatch](
	[ID] [int] IDENTITY(1,1) NOT NULL,
	[ImagingTaskID] [int] NOT NULL,
 CONSTRAINT [PK_ImageBatch] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY],
 CONSTRAINT [IX_ImageBatch] UNIQUE NONCLUSTERED 
(
	[ImagingTaskID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[CaptureProfileVersion]    Script Date: 01/19/2012 13:13:28 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[CaptureProfileVersion](
	[ID] [int] IDENTITY(1,1) NOT NULL,
	[CaptureProfileID] [int] NOT NULL,
	[DateCreated] [datetime] NOT NULL,
 CONSTRAINT [PK_CaptureProfileVersion] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[ImagingScheduleEntryCaptureProfile]    Script Date: 01/19/2012 13:13:28 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[ImagingScheduleEntryCaptureProfile](
	[ID] [bigint] IDENTITY(1,1) NOT NULL,
	[ImagingScheduleEntryID] [int] NOT NULL,
	[CaptureProfileID] [int] NOT NULL,
 CONSTRAINT [PK_ImagingScheduleEntryCaptureProfile] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[RegionCaptureProfile]    Script Date: 01/19/2012 13:13:28 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[RegionCaptureProfile](
	[ID] [bigint] IDENTITY(1,1) NOT NULL,
	[RegionID] [bigint] NOT NULL,
	[CaptureProfileID] [int] NOT NULL,
	[ImagingEnabled] [bit] NOT NULL,
 CONSTRAINT [PK_RegionCaptureProfile] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[WellDropLayer]    Script Date: 01/19/2012 13:13:28 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[WellDropLayer](
	[ID] [bigint] IDENTITY(1,1) NOT NULL,
	[WellDropID] [bigint] NOT NULL,
	[LayerID] [bigint] NOT NULL,
	[Volume] [float] NOT NULL,
	[WaterVolume] [float] NOT NULL,
	[SourceWellID] [bigint] NULL,
	[WaterSource] [varchar](50) NULL,
 CONSTRAINT [PK_WellDropLayer] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[WellLayerIngredient]    Script Date: 01/19/2012 13:13:28 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[WellLayerIngredient](
	[ID] [bigint] IDENTITY(1,1) NOT NULL,
	[WellLayerID] [bigint] NOT NULL,
	[IngredientIngredientTypeID] [int] NOT NULL,
	[IngredientStockID] [int] NOT NULL,
	[HighPHIngredientStockID] [int] NULL,
	[Concentration] [float] NOT NULL,
	[PreMixedConcentration] [float] NOT NULL,
	[Volume] [float] NOT NULL,
	[PH] [float] NULL,
	[LowPHVolume] [float] NULL,
	[HighPHVolume] [float] NULL,
	[Source] [varchar](50) NULL,
	[HighPHSource] [varchar](50) NULL,
 CONSTRAINT [PK_WellLayerIngredient] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[WellDropLayerIngredient]    Script Date: 01/19/2012 13:13:28 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[WellDropLayerIngredient](
	[ID] [bigint] IDENTITY(1,1) NOT NULL,
	[WellDropLayerID] [bigint] NOT NULL,
	[IngredientIngredientTypeID] [int] NOT NULL,
	[IngredientStockID] [int] NOT NULL,
	[HighPHIngredientStockID] [int] NULL,
	[Concentration] [float] NOT NULL,
	[PreMixedConcentration] [float] NOT NULL,
	[Volume] [float] NOT NULL,
	[PH] [float] NULL,
	[LowPHVolume] [float] NULL,
	[HighPHVolume] [float] NULL,
	[Source] [varchar](50) NULL,
	[HighPHSource] [varchar](50) NULL,
 CONSTRAINT [PK_WellDropLayerIngredient] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[ImageBatchWellDropScore]    Script Date: 01/19/2012 13:13:28 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[ImageBatchWellDropScore](
	[ID] [int] IDENTITY(1,1) NOT NULL,
	[WellDropID] [bigint] NOT NULL,
	[ImageBatchID] [int] NOT NULL,
	[WellDropScoreID] [int] NOT NULL,
 CONSTRAINT [PK_ImageBatchWellDropScore] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[CaptureResult]    Script Date: 01/19/2012 13:13:28 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[CaptureResult](
	[ID] [bigint] IDENTITY(1,1) NOT NULL,
	[RegionID] [bigint] NOT NULL,
	[CaptureProfileVersionID] [int] NOT NULL,
	[ImageBatchID] [int] NOT NULL,
	[XCenter] [float] NULL,
	[YCenter] [float] NULL,
	[ZHeight] [float] NULL,
	[XWidth] [float] NULL,
	[YHeight] [float] NULL,
 CONSTRAINT [PK_CaptureResult] PRIMARY KEY NONCLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[CaptureProperty]    Script Date: 01/19/2012 13:13:28 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[CaptureProperty](
	[ID] [bigint] IDENTITY(1,1) NOT NULL,
	[CaptureProfileVersionID] [int] NOT NULL,
	[RobotID] [int] NOT NULL,
	[Name] [varchar](100) NOT NULL,
	[Value] [varchar](1024) NOT NULL,
 CONSTRAINT [PK_CaptureProperty] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[Image]    Script Date: 01/19/2012 13:13:28 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Image](
	[ID] [bigint] IDENTITY(1,1) NOT NULL,
	[CaptureResultID] [bigint] NOT NULL,
	[ImageStoreID] [int] NOT NULL,
	[ImageTypeID] [int] NOT NULL,
	[ImageTypeIndex] [int] NOT NULL,
	[PixelSize] [float] NULL,
	[XCenterOffset] [float] NULL,
	[YCenterOffset] [float] NULL,
	[ZHeight] [float] NULL,
	[FileSize] [int] NULL,
 CONSTRAINT [PK_Image] PRIMARY KEY NONCLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[FocalPoint]    Script Date: 01/19/2012 13:13:28 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[FocalPoint](
	[ID] [bigint] IDENTITY(1,1) NOT NULL,
	[CaptureResultID] [bigint] NOT NULL,
	[Z] [float] NOT NULL,
	[Focus] [float] NOT NULL,
 CONSTRAINT [PK_FocalPoint] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[CaptureResultTuningValues]    Script Date: 01/19/2012 13:13:28 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[CaptureResultTuningValues](
	[ID] [bigint] IDENTITY(1,1) NOT NULL,
	[CaptureResultID] [bigint] NOT NULL,
	[AutoLevelingOn] [bit] NOT NULL,
	[AutoLevelingLowThreshold] [decimal](4, 2) NOT NULL,
	[AutoLevelingHighThreshold] [decimal](4, 2) NOT NULL,
	[LowerLevelLimit] [int] NOT NULL,
	[UpperLevelLimit] [int] NOT NULL,
	[Brightness] [int] NOT NULL,
	[Contrast] [int] NOT NULL,
	[Gamma] [int] NOT NULL,
 CONSTRAINT [PK_CaptureResultTuningValues] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[CaptureResultProperty]    Script Date: 01/19/2012 13:13:28 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[CaptureResultProperty](
	[ID] [bigint] IDENTITY(1,1) NOT NULL,
	[CaptureResultID] [bigint] NOT NULL,
	[Name] [varchar](100) NOT NULL,
	[Value] [varchar](1024) NOT NULL,
 CONSTRAINT [PK_CaptureResultProperty] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[ImageProperty]    Script Date: 01/19/2012 13:13:28 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[ImageProperty](
	[ID] [bigint] IDENTITY(1,1) NOT NULL,
	[ImageID] [bigint] NOT NULL,
	[Name] [varchar](100) NOT NULL,
	[Value] [varchar](1024) NOT NULL,
 CONSTRAINT [PK_ImageProperty] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Default [DF__Tmp_Ingre__Ingre__12E8C319]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[IngredientType] ADD  DEFAULT (0) FOR [IngredientTypeColorID]
GO
/****** Object:  Default [DF__Layer__VolumeBas__2042BE37]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[Layer] ADD  DEFAULT (1) FOR [VolumeBasedAddition]
GO
/****** Object:  Default [DF_Layer_DispenseManually]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[Layer] ADD  CONSTRAINT [DF_Layer_DispenseManually]  DEFAULT (0) FOR [DispenseManually]
GO
/****** Object:  Default [DF__Tmp_WellD__Volum__17AD7836]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[WellDrop] ADD  DEFAULT (0) FOR [Volume]
GO
/****** Object:  Default [DF__Tmp_WellD__Water__18A19C6F]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[WellDrop] ADD  DEFAULT (0) FOR [WaterVolume]
GO
/****** Object:  ForeignKey [FK_BufferDataPoint_Ingredient]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[BufferDataPoint]  WITH NOCHECK ADD  CONSTRAINT [FK_BufferDataPoint_Ingredient] FOREIGN KEY([IngredientID])
REFERENCES [dbo].[Ingredient] ([ID])
GO
ALTER TABLE [dbo].[BufferDataPoint] CHECK CONSTRAINT [FK_BufferDataPoint_Ingredient]
GO
/****** Object:  ForeignKey [FK_CaptureProfile_ExperimentPlate]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[CaptureProfile]  WITH CHECK ADD  CONSTRAINT [FK_CaptureProfile_ExperimentPlate] FOREIGN KEY([ExperimentPlateID])
REFERENCES [dbo].[ExperimentPlate] ([ID])
GO
ALTER TABLE [dbo].[CaptureProfile] CHECK CONSTRAINT [FK_CaptureProfile_ExperimentPlate]
GO
/****** Object:  ForeignKey [FK_CaptureProfileVersion_CaptureProfile]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[CaptureProfileVersion]  WITH NOCHECK ADD  CONSTRAINT [FK_CaptureProfileVersion_CaptureProfile] FOREIGN KEY([CaptureProfileID])
REFERENCES [dbo].[CaptureProfile] ([ID])
GO
ALTER TABLE [dbo].[CaptureProfileVersion] CHECK CONSTRAINT [FK_CaptureProfileVersion_CaptureProfile]
GO
/****** Object:  ForeignKey [FK_CaptureProperty_CaptureProfileVersion]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[CaptureProperty]  WITH CHECK ADD  CONSTRAINT [FK_CaptureProperty_CaptureProfileVersion] FOREIGN KEY([CaptureProfileVersionID])
REFERENCES [dbo].[CaptureProfileVersion] ([ID])
GO
ALTER TABLE [dbo].[CaptureProperty] CHECK CONSTRAINT [FK_CaptureProperty_CaptureProfileVersion]
GO
/****** Object:  ForeignKey [FK_CaptureResult_CaptureProfileVersion]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[CaptureResult]  WITH CHECK ADD  CONSTRAINT [FK_CaptureResult_CaptureProfileVersion] FOREIGN KEY([CaptureProfileVersionID])
REFERENCES [dbo].[CaptureProfileVersion] ([ID])
GO
ALTER TABLE [dbo].[CaptureResult] CHECK CONSTRAINT [FK_CaptureResult_CaptureProfileVersion]
GO
/****** Object:  ForeignKey [FK_CaptureResult_ImageBatch]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[CaptureResult]  WITH CHECK ADD  CONSTRAINT [FK_CaptureResult_ImageBatch] FOREIGN KEY([ImageBatchID])
REFERENCES [dbo].[ImageBatch] ([ID])
GO
ALTER TABLE [dbo].[CaptureResult] CHECK CONSTRAINT [FK_CaptureResult_ImageBatch]
GO
/****** Object:  ForeignKey [FK_CaptureResult_Region]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[CaptureResult]  WITH CHECK ADD  CONSTRAINT [FK_CaptureResult_Region] FOREIGN KEY([RegionID])
REFERENCES [dbo].[Region] ([ID])
GO
ALTER TABLE [dbo].[CaptureResult] CHECK CONSTRAINT [FK_CaptureResult_Region]
GO
/****** Object:  ForeignKey [FK_CaptureResultProperty_CaptureResult]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[CaptureResultProperty]  WITH CHECK ADD  CONSTRAINT [FK_CaptureResultProperty_CaptureResult] FOREIGN KEY([CaptureResultID])
REFERENCES [dbo].[CaptureResult] ([ID])
GO
ALTER TABLE [dbo].[CaptureResultProperty] CHECK CONSTRAINT [FK_CaptureResultProperty_CaptureResult]
GO
/****** Object:  ForeignKey [FK_CaptureResultTuningValues_CaptureResult]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[CaptureResultTuningValues]  WITH CHECK ADD  CONSTRAINT [FK_CaptureResultTuningValues_CaptureResult] FOREIGN KEY([CaptureResultID])
REFERENCES [dbo].[CaptureResult] ([ID])
GO
ALTER TABLE [dbo].[CaptureResultTuningValues] CHECK CONSTRAINT [FK_CaptureResultTuningValues_CaptureResult]
GO
/****** Object:  ForeignKey [FK_CASNumber_Ingredient]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[CASNumber]  WITH NOCHECK ADD  CONSTRAINT [FK_CASNumber_Ingredient] FOREIGN KEY([IngredientID])
REFERENCES [dbo].[Ingredient] ([ID])
GO
ALTER TABLE [dbo].[CASNumber] CHECK CONSTRAINT [FK_CASNumber_Ingredient]
GO
/****** Object:  ForeignKey [FK_Condition_ConditionListLayer]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[Condition]  WITH CHECK ADD  CONSTRAINT [FK_Condition_ConditionListLayer] FOREIGN KEY([ConditionListLayerID])
REFERENCES [dbo].[ConditionListLayer] ([ID])
GO
ALTER TABLE [dbo].[Condition] CHECK CONSTRAINT [FK_Condition_ConditionListLayer]
GO
/****** Object:  ForeignKey [FK_ConditionIngredient_Condition]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[ConditionIngredient]  WITH CHECK ADD  CONSTRAINT [FK_ConditionIngredient_Condition] FOREIGN KEY([ConditionID])
REFERENCES [dbo].[Condition] ([ID])
GO
ALTER TABLE [dbo].[ConditionIngredient] CHECK CONSTRAINT [FK_ConditionIngredient_Condition]
GO
/****** Object:  ForeignKey [FK_ConditionIngredient_IngredientIngredientType]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[ConditionIngredient]  WITH CHECK ADD  CONSTRAINT [FK_ConditionIngredient_IngredientIngredientType] FOREIGN KEY([IngredientIngredientTypeID])
REFERENCES [dbo].[IngredientIngredientType] ([ID])
GO
ALTER TABLE [dbo].[ConditionIngredient] CHECK CONSTRAINT [FK_ConditionIngredient_IngredientIngredientType]
GO
/****** Object:  ForeignKey [FK_ConditionIngredient_IngredientStock]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[ConditionIngredient]  WITH CHECK ADD  CONSTRAINT [FK_ConditionIngredient_IngredientStock] FOREIGN KEY([IngredientStockID])
REFERENCES [dbo].[IngredientStock] ([ID])
GO
ALTER TABLE [dbo].[ConditionIngredient] CHECK CONSTRAINT [FK_ConditionIngredient_IngredientStock]
GO
/****** Object:  ForeignKey [FK_ConditionIngredient_IngredientStock_HighPH]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[ConditionIngredient]  WITH CHECK ADD  CONSTRAINT [FK_ConditionIngredient_IngredientStock_HighPH] FOREIGN KEY([HighPHIngredientStockID])
REFERENCES [dbo].[IngredientStock] ([ID])
GO
ALTER TABLE [dbo].[ConditionIngredient] CHECK CONSTRAINT [FK_ConditionIngredient_IngredientStock_HighPH]
GO
/****** Object:  ForeignKey [FK_ConditionListLayer_Experiment]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[ConditionListLayer]  WITH CHECK ADD  CONSTRAINT [FK_ConditionListLayer_Experiment] FOREIGN KEY([ExperimentID])
REFERENCES [dbo].[Experiment] ([ID])
GO
ALTER TABLE [dbo].[ConditionListLayer] CHECK CONSTRAINT [FK_ConditionListLayer_Experiment]
GO
/****** Object:  ForeignKey [FK_ConditionListLayer_Layer]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[ConditionListLayer]  WITH CHECK ADD  CONSTRAINT [FK_ConditionListLayer_Layer] FOREIGN KEY([LayerID])
REFERENCES [dbo].[Layer] ([ID])
GO
ALTER TABLE [dbo].[ConditionListLayer] CHECK CONSTRAINT [FK_ConditionListLayer_Layer]
GO
/****** Object:  ForeignKey [FK_ContainerDrop_Containers]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[ContainerDrop]  WITH NOCHECK ADD  CONSTRAINT [FK_ContainerDrop_Containers] FOREIGN KEY([ContainerID])
REFERENCES [dbo].[Containers] ([ID])
GO
ALTER TABLE [dbo].[ContainerDrop] CHECK CONSTRAINT [FK_ContainerDrop_Containers]
GO
/****** Object:  ForeignKey [FK_Deck_DispensingRobot]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[Deck]  WITH CHECK ADD  CONSTRAINT [FK_Deck_DispensingRobot] FOREIGN KEY([DispensingRobotID])
REFERENCES [dbo].[DispensingRobot] ([ID])
GO
ALTER TABLE [dbo].[Deck] CHECK CONSTRAINT [FK_Deck_DispensingRobot]
GO
/****** Object:  ForeignKey [FK_Deck_LidStorage]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[Deck]  WITH CHECK ADD  CONSTRAINT [FK_Deck_LidStorage] FOREIGN KEY([LidStorageID])
REFERENCES [dbo].[LidStorage] ([ID])
GO
ALTER TABLE [dbo].[Deck] CHECK CONSTRAINT [FK_Deck_LidStorage]
GO
/****** Object:  ForeignKey [FK_Deck_Rack]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[Deck]  WITH CHECK ADD  CONSTRAINT [FK_Deck_Rack] FOREIGN KEY([RackID])
REFERENCES [dbo].[Rack] ([ID])
GO
ALTER TABLE [dbo].[Deck] CHECK CONSTRAINT [FK_Deck_Rack]
GO
/****** Object:  ForeignKey [FK_DispenseQueue_DispensingRobot]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[DispenseQueue]  WITH CHECK ADD  CONSTRAINT [FK_DispenseQueue_DispensingRobot] FOREIGN KEY([DispensingRobotID])
REFERENCES [dbo].[DispensingRobot] ([ID])
GO
ALTER TABLE [dbo].[DispenseQueue] CHECK CONSTRAINT [FK_DispenseQueue_DispensingRobot]
GO
/****** Object:  ForeignKey [FK_DispenseQueue_Plate]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[DispenseQueue]  WITH CHECK ADD  CONSTRAINT [FK_DispenseQueue_Plate] FOREIGN KEY([PlateID])
REFERENCES [dbo].[Plate] ([ID])
GO
ALTER TABLE [dbo].[DispenseQueue] CHECK CONSTRAINT [FK_DispenseQueue_Plate]
GO
/****** Object:  ForeignKey [FK_DriverProperty_DispensingRobot]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[DriverProperty]  WITH NOCHECK ADD  CONSTRAINT [FK_DriverProperty_DispensingRobot] FOREIGN KEY([DispensingRobotID])
REFERENCES [dbo].[DispensingRobot] ([ID])
GO
ALTER TABLE [dbo].[DriverProperty] CHECK CONSTRAINT [FK_DriverProperty_DispensingRobot]
GO
/****** Object:  ForeignKey [FK_Experiment_Container]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[Experiment]  WITH CHECK ADD  CONSTRAINT [FK_Experiment_Container] FOREIGN KEY([ContainerID])
REFERENCES [dbo].[Containers] ([ID])
GO
ALTER TABLE [dbo].[Experiment] CHECK CONSTRAINT [FK_Experiment_Container]
GO
/****** Object:  ForeignKey [FK_Experiment_ImagingSchedule]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[Experiment]  WITH CHECK ADD  CONSTRAINT [FK_Experiment_ImagingSchedule] FOREIGN KEY([ImagingScheduleID])
REFERENCES [dbo].[ImagingSchedule] ([ID])
GO
ALTER TABLE [dbo].[Experiment] CHECK CONSTRAINT [FK_Experiment_ImagingSchedule]
GO
/****** Object:  ForeignKey [FK_Experiment_TreeNode]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[Experiment]  WITH CHECK ADD  CONSTRAINT [FK_Experiment_TreeNode] FOREIGN KEY([TreeNodeID])
REFERENCES [dbo].[TreeNode] ([ID])
GO
ALTER TABLE [dbo].[Experiment] CHECK CONSTRAINT [FK_Experiment_TreeNode]
GO
/****** Object:  ForeignKey [FK_Experiment_Users]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[Experiment]  WITH CHECK ADD  CONSTRAINT [FK_Experiment_Users] FOREIGN KEY([UserID])
REFERENCES [dbo].[Users] ([ID])
GO
ALTER TABLE [dbo].[Experiment] CHECK CONSTRAINT [FK_Experiment_Users]
GO
/****** Object:  ForeignKey [FK_ExperimentCaptureProfileTuningValues_CaptureProfile]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[ExperimentCaptureProfileTuningValues]  WITH CHECK ADD  CONSTRAINT [FK_ExperimentCaptureProfileTuningValues_CaptureProfile] FOREIGN KEY([CaptureProfileID])
REFERENCES [dbo].[CaptureProfile] ([ID])
GO
ALTER TABLE [dbo].[ExperimentCaptureProfileTuningValues] CHECK CONSTRAINT [FK_ExperimentCaptureProfileTuningValues_CaptureProfile]
GO
/****** Object:  ForeignKey [FK_ExperimentCaptureProfileTuningValues_Experiment]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[ExperimentCaptureProfileTuningValues]  WITH CHECK ADD  CONSTRAINT [FK_ExperimentCaptureProfileTuningValues_Experiment] FOREIGN KEY([ExperimentID])
REFERENCES [dbo].[Experiment] ([ID])
GO
ALTER TABLE [dbo].[ExperimentCaptureProfileTuningValues] CHECK CONSTRAINT [FK_ExperimentCaptureProfileTuningValues_Experiment]
GO
/****** Object:  ForeignKey [FK_ExperimentPlate_Plate]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[ExperimentPlate]  WITH CHECK ADD  CONSTRAINT [FK_ExperimentPlate_Plate] FOREIGN KEY([PlateID])
REFERENCES [dbo].[Plate] ([ID])
GO
ALTER TABLE [dbo].[ExperimentPlate] CHECK CONSTRAINT [FK_ExperimentPlate_Plate]
GO
/****** Object:  ForeignKey [FK_FavoriteIngredientStock_IngredientIngredientType]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[FavoriteIngredientStock]  WITH CHECK ADD  CONSTRAINT [FK_FavoriteIngredientStock_IngredientIngredientType] FOREIGN KEY([IngredientIngredientTypeID])
REFERENCES [dbo].[IngredientIngredientType] ([ID])
GO
ALTER TABLE [dbo].[FavoriteIngredientStock] CHECK CONSTRAINT [FK_FavoriteIngredientStock_IngredientIngredientType]
GO
/****** Object:  ForeignKey [FK_FavoriteIngredientStock_IngredientStock]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[FavoriteIngredientStock]  WITH CHECK ADD  CONSTRAINT [FK_FavoriteIngredientStock_IngredientStock] FOREIGN KEY([IngredientStockID])
REFERENCES [dbo].[IngredientStock] ([ID])
GO
ALTER TABLE [dbo].[FavoriteIngredientStock] CHECK CONSTRAINT [FK_FavoriteIngredientStock_IngredientStock]
GO
/****** Object:  ForeignKey [FK_FavoriteIngredientStock_Users]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[FavoriteIngredientStock]  WITH CHECK ADD  CONSTRAINT [FK_FavoriteIngredientStock_Users] FOREIGN KEY([UserID])
REFERENCES [dbo].[Users] ([ID])
GO
ALTER TABLE [dbo].[FavoriteIngredientStock] CHECK CONSTRAINT [FK_FavoriteIngredientStock_Users]
GO
/****** Object:  ForeignKey [FK_Filter_Search]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[Filter]  WITH CHECK ADD  CONSTRAINT [FK_Filter_Search] FOREIGN KEY([SearchID])
REFERENCES [dbo].[Search] ([ID])
GO
ALTER TABLE [dbo].[Filter] CHECK CONSTRAINT [FK_Filter_Search]
GO
/****** Object:  ForeignKey [FK_Filter_SearchField]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[Filter]  WITH CHECK ADD  CONSTRAINT [FK_Filter_SearchField] FOREIGN KEY([SearchFieldID])
REFERENCES [dbo].[SearchField] ([ID])
GO
ALTER TABLE [dbo].[Filter] CHECK CONSTRAINT [FK_Filter_SearchField]
GO
/****** Object:  ForeignKey [FK_FocalPoint_CaptureResult]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[FocalPoint]  WITH CHECK ADD  CONSTRAINT [FK_FocalPoint_CaptureResult] FOREIGN KEY([CaptureResultID])
REFERENCES [dbo].[CaptureResult] ([ID])
GO
ALTER TABLE [dbo].[FocalPoint] CHECK CONSTRAINT [FK_FocalPoint_CaptureResult]
GO
/****** Object:  ForeignKey [FK_Grid_Experiment]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[Grid]  WITH CHECK ADD  CONSTRAINT [FK_Grid_Experiment] FOREIGN KEY([ExperimentID])
REFERENCES [dbo].[Experiment] ([ID])
GO
ALTER TABLE [dbo].[Grid] CHECK CONSTRAINT [FK_Grid_Experiment]
GO
/****** Object:  ForeignKey [FK_Grid_GridVaryAlong]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[Grid]  WITH CHECK ADD  CONSTRAINT [FK_Grid_GridVaryAlong] FOREIGN KEY([WellVolumeVaryAlong])
REFERENCES [dbo].[GridVaryAlong] ([ID])
GO
ALTER TABLE [dbo].[Grid] CHECK CONSTRAINT [FK_Grid_GridVaryAlong]
GO
/****** Object:  ForeignKey [FK_GridIngredient_Grid]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[GridIngredient]  WITH CHECK ADD  CONSTRAINT [FK_GridIngredient_Grid] FOREIGN KEY([GridID])
REFERENCES [dbo].[Grid] ([ID])
GO
ALTER TABLE [dbo].[GridIngredient] CHECK CONSTRAINT [FK_GridIngredient_Grid]
GO
/****** Object:  ForeignKey [FK_GridIngredient_GridVaryAlong_ConcentrationVaryAlong]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[GridIngredient]  WITH CHECK ADD  CONSTRAINT [FK_GridIngredient_GridVaryAlong_ConcentrationVaryAlong] FOREIGN KEY([ConcentrationVaryAlong])
REFERENCES [dbo].[GridVaryAlong] ([ID])
GO
ALTER TABLE [dbo].[GridIngredient] CHECK CONSTRAINT [FK_GridIngredient_GridVaryAlong_ConcentrationVaryAlong]
GO
/****** Object:  ForeignKey [FK_GridIngredient_GridVaryAlong_pHVaryAlong]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[GridIngredient]  WITH CHECK ADD  CONSTRAINT [FK_GridIngredient_GridVaryAlong_pHVaryAlong] FOREIGN KEY([pHVaryAlong])
REFERENCES [dbo].[GridVaryAlong] ([ID])
GO
ALTER TABLE [dbo].[GridIngredient] CHECK CONSTRAINT [FK_GridIngredient_GridVaryAlong_pHVaryAlong]
GO
/****** Object:  ForeignKey [FK_GridIngredient_IngredientIngredientType]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[GridIngredient]  WITH CHECK ADD  CONSTRAINT [FK_GridIngredient_IngredientIngredientType] FOREIGN KEY([IngredientIngredientTypeID])
REFERENCES [dbo].[IngredientIngredientType] ([ID])
GO
ALTER TABLE [dbo].[GridIngredient] CHECK CONSTRAINT [FK_GridIngredient_IngredientIngredientType]
GO
/****** Object:  ForeignKey [FK_GridIngredient_IngredientStock]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[GridIngredient]  WITH CHECK ADD  CONSTRAINT [FK_GridIngredient_IngredientStock] FOREIGN KEY([IngredientStockID])
REFERENCES [dbo].[IngredientStock] ([ID])
GO
ALTER TABLE [dbo].[GridIngredient] CHECK CONSTRAINT [FK_GridIngredient_IngredientStock]
GO
/****** Object:  ForeignKey [FK_GridIngredient_IngredientStock_HighPH]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[GridIngredient]  WITH CHECK ADD  CONSTRAINT [FK_GridIngredient_IngredientStock_HighPH] FOREIGN KEY([HighPHIngredientStockID])
REFERENCES [dbo].[IngredientStock] ([ID])
GO
ALTER TABLE [dbo].[GridIngredient] CHECK CONSTRAINT [FK_GridIngredient_IngredientStock_HighPH]
GO
/****** Object:  ForeignKey [FK_GridLayer_Grid]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[GridLayer]  WITH CHECK ADD  CONSTRAINT [FK_GridLayer_Grid] FOREIGN KEY([GridID])
REFERENCES [dbo].[Grid] ([ID])
GO
ALTER TABLE [dbo].[GridLayer] CHECK CONSTRAINT [FK_GridLayer_Grid]
GO
/****** Object:  ForeignKey [FK_GridLayer_Layer]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[GridLayer]  WITH CHECK ADD  CONSTRAINT [FK_GridLayer_Layer] FOREIGN KEY([LayerID])
REFERENCES [dbo].[Layer] ([ID])
GO
ALTER TABLE [dbo].[GridLayer] CHECK CONSTRAINT [FK_GridLayer_Layer]
GO
/****** Object:  ForeignKey [FK_GroupUser_Group]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[GroupUser]  WITH NOCHECK ADD  CONSTRAINT [FK_GroupUser_Group] FOREIGN KEY([GroupID])
REFERENCES [dbo].[Groups] ([ID])
GO
ALTER TABLE [dbo].[GroupUser] CHECK CONSTRAINT [FK_GroupUser_Group]
GO
/****** Object:  ForeignKey [FK_GroupUser_Users]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[GroupUser]  WITH NOCHECK ADD  CONSTRAINT [FK_GroupUser_Users] FOREIGN KEY([UserID])
REFERENCES [dbo].[Users] ([ID])
GO
ALTER TABLE [dbo].[GroupUser] CHECK CONSTRAINT [FK_GroupUser_Users]
GO
/****** Object:  ForeignKey [FK_Image_CaptureResult]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[Image]  WITH CHECK ADD  CONSTRAINT [FK_Image_CaptureResult] FOREIGN KEY([CaptureResultID])
REFERENCES [dbo].[CaptureResult] ([ID])
GO
ALTER TABLE [dbo].[Image] CHECK CONSTRAINT [FK_Image_CaptureResult]
GO
/****** Object:  ForeignKey [FK_Image_ImageStore]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[Image]  WITH CHECK ADD  CONSTRAINT [FK_Image_ImageStore] FOREIGN KEY([ImageStoreID])
REFERENCES [dbo].[ImageStore] ([ID])
GO
ALTER TABLE [dbo].[Image] CHECK CONSTRAINT [FK_Image_ImageStore]
GO
/****** Object:  ForeignKey [FK_Image_ImageType]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[Image]  WITH CHECK ADD  CONSTRAINT [FK_Image_ImageType] FOREIGN KEY([ImageTypeID])
REFERENCES [dbo].[ImageType] ([ID])
GO
ALTER TABLE [dbo].[Image] CHECK CONSTRAINT [FK_Image_ImageType]
GO
/****** Object:  ForeignKey [FK_ImageBatch_ImagingTask]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[ImageBatch]  WITH CHECK ADD  CONSTRAINT [FK_ImageBatch_ImagingTask] FOREIGN KEY([ImagingTaskID])
REFERENCES [dbo].[ImagingTask] ([ID])
GO
ALTER TABLE [dbo].[ImageBatch] CHECK CONSTRAINT [FK_ImageBatch_ImagingTask]
GO
/****** Object:  ForeignKey [FK_ImageBatchWellDropScore_ImageBatch]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[ImageBatchWellDropScore]  WITH CHECK ADD  CONSTRAINT [FK_ImageBatchWellDropScore_ImageBatch] FOREIGN KEY([ImageBatchID])
REFERENCES [dbo].[ImageBatch] ([ID])
GO
ALTER TABLE [dbo].[ImageBatchWellDropScore] CHECK CONSTRAINT [FK_ImageBatchWellDropScore_ImageBatch]
GO
/****** Object:  ForeignKey [FK_ImageBatchWellDropScore_WellDropScore]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[ImageBatchWellDropScore]  WITH CHECK ADD  CONSTRAINT [FK_ImageBatchWellDropScore_WellDropScore] FOREIGN KEY([WellDropScoreID])
REFERENCES [dbo].[WellDropScore] ([ID])
GO
ALTER TABLE [dbo].[ImageBatchWellDropScore] CHECK CONSTRAINT [FK_ImageBatchWellDropScore_WellDropScore]
GO
/****** Object:  ForeignKey [FK_ImageManagement_TreeNode]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[ImageManagement]  WITH NOCHECK ADD  CONSTRAINT [FK_ImageManagement_TreeNode] FOREIGN KEY([TreeNodeID])
REFERENCES [dbo].[TreeNode] ([ID])
GO
ALTER TABLE [dbo].[ImageManagement] CHECK CONSTRAINT [FK_ImageManagement_TreeNode]
GO
/****** Object:  ForeignKey [FK_ImageManagementAction_ImageManagement]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[ImageManagementAction]  WITH NOCHECK ADD  CONSTRAINT [FK_ImageManagementAction_ImageManagement] FOREIGN KEY([ImageManagementID])
REFERENCES [dbo].[ImageManagement] ([ID])
GO
ALTER TABLE [dbo].[ImageManagementAction] CHECK CONSTRAINT [FK_ImageManagementAction_ImageManagement]
GO
/****** Object:  ForeignKey [FK_ImageManagementAction_SearchConstraintTreeNode]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[ImageManagementAction]  WITH NOCHECK ADD  CONSTRAINT [FK_ImageManagementAction_SearchConstraintTreeNode] FOREIGN KEY([RootSearchConstraintTreeNodeID])
REFERENCES [dbo].[SearchConstraintTreeNode] ([ID])
GO
ALTER TABLE [dbo].[ImageManagementAction] CHECK CONSTRAINT [FK_ImageManagementAction_SearchConstraintTreeNode]
GO
/****** Object:  ForeignKey [FK_ImageManagementAction_SourceImageStore]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[ImageManagementAction]  WITH NOCHECK ADD  CONSTRAINT [FK_ImageManagementAction_SourceImageStore] FOREIGN KEY([SourceImageStoreID])
REFERENCES [dbo].[ImageStore] ([ID])
GO
ALTER TABLE [dbo].[ImageManagementAction] CHECK CONSTRAINT [FK_ImageManagementAction_SourceImageStore]
GO
/****** Object:  ForeignKey [FK_ImageManagementAction_TargetImageStore]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[ImageManagementAction]  WITH NOCHECK ADD  CONSTRAINT [FK_ImageManagementAction_TargetImageStore] FOREIGN KEY([TargetImageStoreID])
REFERENCES [dbo].[ImageStore] ([ID])
GO
ALTER TABLE [dbo].[ImageManagementAction] CHECK CONSTRAINT [FK_ImageManagementAction_TargetImageStore]
GO
/****** Object:  ForeignKey [FK_ImageProperty_Image]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[ImageProperty]  WITH CHECK ADD  CONSTRAINT [FK_ImageProperty_Image] FOREIGN KEY([ImageID])
REFERENCES [dbo].[Image] ([ID])
GO
ALTER TABLE [dbo].[ImageProperty] CHECK CONSTRAINT [FK_ImageProperty_Image]
GO
/****** Object:  ForeignKey [FK_ImagerGroups_Groups]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[ImagerGroups]  WITH CHECK ADD  CONSTRAINT [FK_ImagerGroups_Groups] FOREIGN KEY([GroupID])
REFERENCES [dbo].[Groups] ([ID])
GO
ALTER TABLE [dbo].[ImagerGroups] CHECK CONSTRAINT [FK_ImagerGroups_Groups]
GO
/****** Object:  ForeignKey [FK_ImagerGroups_Imager]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[ImagerGroups]  WITH CHECK ADD  CONSTRAINT [FK_ImagerGroups_Imager] FOREIGN KEY([ImagerID])
REFERENCES [dbo].[Imager] ([ID])
GO
ALTER TABLE [dbo].[ImagerGroups] CHECK CONSTRAINT [FK_ImagerGroups_Imager]
GO
/****** Object:  ForeignKey [FK_ImageStore_ImageStoreLayout]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[ImageStore]  WITH NOCHECK ADD  CONSTRAINT [FK_ImageStore_ImageStoreLayout] FOREIGN KEY([ImageStoreLayoutID])
REFERENCES [dbo].[ImageStoreLayout] ([ID])
GO
ALTER TABLE [dbo].[ImageStore] CHECK CONSTRAINT [FK_ImageStore_ImageStoreLayout]
GO
/****** Object:  ForeignKey [FK_ImagingScheduleEntry_ImagingSchedule]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[ImagingScheduleEntry]  WITH NOCHECK ADD  CONSTRAINT [FK_ImagingScheduleEntry_ImagingSchedule] FOREIGN KEY([ImagingScheduleID])
REFERENCES [dbo].[ImagingSchedule] ([ID])
GO
ALTER TABLE [dbo].[ImagingScheduleEntry] CHECK CONSTRAINT [FK_ImagingScheduleEntry_ImagingSchedule]
GO
/****** Object:  ForeignKey [FK_ImagingScheduleEntryCaptureProfile_CaptureProfile]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[ImagingScheduleEntryCaptureProfile]  WITH CHECK ADD  CONSTRAINT [FK_ImagingScheduleEntryCaptureProfile_CaptureProfile] FOREIGN KEY([CaptureProfileID])
REFERENCES [dbo].[CaptureProfile] ([ID])
GO
ALTER TABLE [dbo].[ImagingScheduleEntryCaptureProfile] CHECK CONSTRAINT [FK_ImagingScheduleEntryCaptureProfile_CaptureProfile]
GO
/****** Object:  ForeignKey [FK_ImagingScheduleEntryCaptureProfile_ImagingScheduleEntry]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[ImagingScheduleEntryCaptureProfile]  WITH CHECK ADD  CONSTRAINT [FK_ImagingScheduleEntryCaptureProfile_ImagingScheduleEntry] FOREIGN KEY([ImagingScheduleEntryID])
REFERENCES [dbo].[ImagingScheduleEntry] ([ID])
GO
ALTER TABLE [dbo].[ImagingScheduleEntryCaptureProfile] CHECK CONSTRAINT [FK_ImagingScheduleEntryCaptureProfile_ImagingScheduleEntry]
GO
/****** Object:  ForeignKey [FK_ImagingTask_ExperimentPlate]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[ImagingTask]  WITH CHECK ADD  CONSTRAINT [FK_ImagingTask_ExperimentPlate] FOREIGN KEY([ExperimentPlateID])
REFERENCES [dbo].[ExperimentPlate] ([ID])
GO
ALTER TABLE [dbo].[ImagingTask] CHECK CONSTRAINT [FK_ImagingTask_ExperimentPlate]
GO
/****** Object:  ForeignKey [FK_ImagingTask_ImagingScheduleEntry]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[ImagingTask]  WITH CHECK ADD  CONSTRAINT [FK_ImagingTask_ImagingScheduleEntry] FOREIGN KEY([ImagingScheduleEntryID])
REFERENCES [dbo].[ImagingScheduleEntry] ([ID])
GO
ALTER TABLE [dbo].[ImagingTask] CHECK CONSTRAINT [FK_ImagingTask_ImagingScheduleEntry]
GO
/****** Object:  ForeignKey [FK_Ingredient_BufferCalculationType]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[Ingredient]  WITH NOCHECK ADD  CONSTRAINT [FK_Ingredient_BufferCalculationType] FOREIGN KEY([BufferCalculationTypeID])
REFERENCES [dbo].[BufferCalculationType] ([ID])
GO
ALTER TABLE [dbo].[Ingredient] CHECK CONSTRAINT [FK_Ingredient_BufferCalculationType]
GO
/****** Object:  ForeignKey [FK_IngredientAlias_Ingredient]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[IngredientAlias]  WITH NOCHECK ADD  CONSTRAINT [FK_IngredientAlias_Ingredient] FOREIGN KEY([IngredientID])
REFERENCES [dbo].[Ingredient] ([ID])
GO
ALTER TABLE [dbo].[IngredientAlias] CHECK CONSTRAINT [FK_IngredientAlias_Ingredient]
GO
/****** Object:  ForeignKey [FK_IngredientIngredientType_Ingredient]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[IngredientIngredientType]  WITH NOCHECK ADD  CONSTRAINT [FK_IngredientIngredientType_Ingredient] FOREIGN KEY([IngredientID])
REFERENCES [dbo].[Ingredient] ([ID])
GO
ALTER TABLE [dbo].[IngredientIngredientType] CHECK CONSTRAINT [FK_IngredientIngredientType_Ingredient]
GO
/****** Object:  ForeignKey [FK_IngredientIngredientType_IngredientType]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[IngredientIngredientType]  WITH CHECK ADD  CONSTRAINT [FK_IngredientIngredientType_IngredientType] FOREIGN KEY([IngredientTypeID])
REFERENCES [dbo].[IngredientType] ([ID])
GO
ALTER TABLE [dbo].[IngredientIngredientType] CHECK CONSTRAINT [FK_IngredientIngredientType_IngredientType]
GO
/****** Object:  ForeignKey [FK_IngredientStock_Ingredient]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[IngredientStock]  WITH NOCHECK ADD  CONSTRAINT [FK_IngredientStock_Ingredient] FOREIGN KEY([IngredientID])
REFERENCES [dbo].[Ingredient] ([ID])
GO
ALTER TABLE [dbo].[IngredientStock] CHECK CONSTRAINT [FK_IngredientStock_Ingredient]
GO
/****** Object:  ForeignKey [FK_IngredientStock_LiquidClass]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[IngredientStock]  WITH NOCHECK ADD  CONSTRAINT [FK_IngredientStock_LiquidClass] FOREIGN KEY([LiquidClassID])
REFERENCES [dbo].[LiquidClass] ([ID])
GO
ALTER TABLE [dbo].[IngredientStock] CHECK CONSTRAINT [FK_IngredientStock_LiquidClass]
GO
/****** Object:  ForeignKey [FK_IngredientStock_Vendor]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[IngredientStock]  WITH NOCHECK ADD  CONSTRAINT [FK_IngredientStock_Vendor] FOREIGN KEY([VendorID])
REFERENCES [dbo].[Vendor] ([ID])
GO
ALTER TABLE [dbo].[IngredientStock] CHECK CONSTRAINT [FK_IngredientStock_Vendor]
GO
/****** Object:  ForeignKey [FK_IngredientType_IngredientTypeColor]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[IngredientType]  WITH CHECK ADD  CONSTRAINT [FK_IngredientType_IngredientTypeColor] FOREIGN KEY([IngredientTypeColorID])
REFERENCES [dbo].[IngredientTypeColor] ([ID])
GO
ALTER TABLE [dbo].[IngredientType] CHECK CONSTRAINT [FK_IngredientType_IngredientTypeColor]
GO
/****** Object:  ForeignKey [FK_LidStorage_LidStorageType]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[LidStorage]  WITH CHECK ADD  CONSTRAINT [FK_LidStorage_LidStorageType] FOREIGN KEY([LidStorageTypeID])
REFERENCES [dbo].[LidStorageType] ([ID])
GO
ALTER TABLE [dbo].[LidStorage] CHECK CONSTRAINT [FK_LidStorage_LidStorageType]
GO
/****** Object:  ForeignKey [FK_LidStorageType_DispensingRobot]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[LidStorageType]  WITH CHECK ADD  CONSTRAINT [FK_LidStorageType_DispensingRobot] FOREIGN KEY([DispensingRobotID])
REFERENCES [dbo].[DispensingRobot] ([ID])
GO
ALTER TABLE [dbo].[LidStorageType] CHECK CONSTRAINT [FK_LidStorageType_DispensingRobot]
GO
/****** Object:  ForeignKey [FK_MicrofluidicsExperiment_Experiment]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[MicrofluidicsExperiment]  WITH CHECK ADD  CONSTRAINT [FK_MicrofluidicsExperiment_Experiment] FOREIGN KEY([ExperimentID])
REFERENCES [dbo].[Experiment] ([ID])
GO
ALTER TABLE [dbo].[MicrofluidicsExperiment] CHECK CONSTRAINT [FK_MicrofluidicsExperiment_Experiment]
GO
/****** Object:  ForeignKey [FK_Plate_DispensingRobot]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[Plate]  WITH CHECK ADD  CONSTRAINT [FK_Plate_DispensingRobot] FOREIGN KEY([DispensingRobotID])
REFERENCES [dbo].[DispensingRobot] ([ID])
GO
ALTER TABLE [dbo].[Plate] CHECK CONSTRAINT [FK_Plate_DispensingRobot]
GO
/****** Object:  ForeignKey [FK_Plate_Experiment]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[Plate]  WITH CHECK ADD  CONSTRAINT [FK_Plate_Experiment] FOREIGN KEY([ExperimentID])
REFERENCES [dbo].[Experiment] ([ID])
GO
ALTER TABLE [dbo].[Plate] CHECK CONSTRAINT [FK_Plate_Experiment]
GO
/****** Object:  ForeignKey [FK_PlateEvents_Plate]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[PlateEvents]  WITH CHECK ADD  CONSTRAINT [FK_PlateEvents_Plate] FOREIGN KEY([PlateID])
REFERENCES [dbo].[Plate] ([ID])
GO
ALTER TABLE [dbo].[PlateEvents] CHECK CONSTRAINT [FK_PlateEvents_Plate]
GO
/****** Object:  ForeignKey [FK_PlateEvents_PlateEventType]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[PlateEvents]  WITH CHECK ADD  CONSTRAINT [FK_PlateEvents_PlateEventType] FOREIGN KEY([PlateEventTypeID])
REFERENCES [dbo].[PlateEventType] ([ID])
GO
ALTER TABLE [dbo].[PlateEvents] CHECK CONSTRAINT [FK_PlateEvents_PlateEventType]
GO
/****** Object:  ForeignKey [FK_Project_ImagingSchedule]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[Project]  WITH NOCHECK ADD  CONSTRAINT [FK_Project_ImagingSchedule] FOREIGN KEY([DefaultImagingScheduleID])
REFERENCES [dbo].[ImagingSchedule] ([ID])
GO
ALTER TABLE [dbo].[Project] CHECK CONSTRAINT [FK_Project_ImagingSchedule]
GO
/****** Object:  ForeignKey [FK_Project_TreeNode]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[Project]  WITH NOCHECK ADD  CONSTRAINT [FK_Project_TreeNode] FOREIGN KEY([TreeNodeID])
REFERENCES [dbo].[TreeNode] ([ID])
GO
ALTER TABLE [dbo].[Project] CHECK CONSTRAINT [FK_Project_TreeNode]
GO
/****** Object:  ForeignKey [FK_Project_Users]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[Project]  WITH NOCHECK ADD  CONSTRAINT [FK_Project_Users] FOREIGN KEY([OwnerUserID])
REFERENCES [dbo].[Users] ([ID])
GO
ALTER TABLE [dbo].[Project] CHECK CONSTRAINT [FK_Project_Users]
GO
/****** Object:  ForeignKey [FK_PropertyListValue_PropertyListType]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[PropertyListValue]  WITH NOCHECK ADD  CONSTRAINT [FK_PropertyListValue_PropertyListType] FOREIGN KEY([PropertyListTypeID])
REFERENCES [dbo].[PropertyListType] ([ID])
GO
ALTER TABLE [dbo].[PropertyListValue] CHECK CONSTRAINT [FK_PropertyListValue_PropertyListType]
GO
/****** Object:  ForeignKey [FK_ProteinFormulation_TreeNode]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[ProteinFormulation]  WITH NOCHECK ADD  CONSTRAINT [FK_ProteinFormulation_TreeNode] FOREIGN KEY([TreeNodeID])
REFERENCES [dbo].[TreeNode] ([ID])
GO
ALTER TABLE [dbo].[ProteinFormulation] CHECK CONSTRAINT [FK_ProteinFormulation_TreeNode]
GO
/****** Object:  ForeignKey [FK_ProteinFormulationAttachment_ProteinFormulation]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[ProteinFormulationAttachment]  WITH CHECK ADD  CONSTRAINT [FK_ProteinFormulationAttachment_ProteinFormulation] FOREIGN KEY([ProteinFormulationID])
REFERENCES [dbo].[ProteinFormulation] ([ID])
GO
ALTER TABLE [dbo].[ProteinFormulationAttachment] CHECK CONSTRAINT [FK_ProteinFormulationAttachment_ProteinFormulation]
GO
/****** Object:  ForeignKey [FK_ProteinFormulationIngredient_ProteinFormulation]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[ProteinFormulationIngredient]  WITH CHECK ADD  CONSTRAINT [FK_ProteinFormulationIngredient_ProteinFormulation] FOREIGN KEY([ProteinFormulationID])
REFERENCES [dbo].[ProteinFormulation] ([ID])
GO
ALTER TABLE [dbo].[ProteinFormulationIngredient] CHECK CONSTRAINT [FK_ProteinFormulationIngredient_ProteinFormulation]
GO
/****** Object:  ForeignKey [FK_ProteinFormulationIngredient_ProteinFormulationType]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[ProteinFormulationIngredient]  WITH CHECK ADD  CONSTRAINT [FK_ProteinFormulationIngredient_ProteinFormulationType] FOREIGN KEY([ProteinFormulationIngredientTypeID])
REFERENCES [dbo].[ProteinFormulationIngredientType] ([ID])
GO
ALTER TABLE [dbo].[ProteinFormulationIngredient] CHECK CONSTRAINT [FK_ProteinFormulationIngredient_ProteinFormulationType]
GO
/****** Object:  ForeignKey [FK_ProteinFormulationStandardIngredient_HighPHIngredientStock]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[ProteinFormulationStandardIngredient]  WITH CHECK ADD  CONSTRAINT [FK_ProteinFormulationStandardIngredient_HighPHIngredientStock] FOREIGN KEY([HighPHIngredientStockID])
REFERENCES [dbo].[IngredientStock] ([ID])
GO
ALTER TABLE [dbo].[ProteinFormulationStandardIngredient] CHECK CONSTRAINT [FK_ProteinFormulationStandardIngredient_HighPHIngredientStock]
GO
/****** Object:  ForeignKey [FK_ProteinFormulationStandardIngredient_IngredientIngredientType]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[ProteinFormulationStandardIngredient]  WITH CHECK ADD  CONSTRAINT [FK_ProteinFormulationStandardIngredient_IngredientIngredientType] FOREIGN KEY([IngredientIngredientTypeID])
REFERENCES [dbo].[IngredientIngredientType] ([ID])
GO
ALTER TABLE [dbo].[ProteinFormulationStandardIngredient] CHECK CONSTRAINT [FK_ProteinFormulationStandardIngredient_IngredientIngredientType]
GO
/****** Object:  ForeignKey [FK_ProteinFormulationStandardIngredient_IngredientStock]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[ProteinFormulationStandardIngredient]  WITH CHECK ADD  CONSTRAINT [FK_ProteinFormulationStandardIngredient_IngredientStock] FOREIGN KEY([IngredientStockID])
REFERENCES [dbo].[IngredientStock] ([ID])
GO
ALTER TABLE [dbo].[ProteinFormulationStandardIngredient] CHECK CONSTRAINT [FK_ProteinFormulationStandardIngredient_IngredientStock]
GO
/****** Object:  ForeignKey [FK_ProteinFormulationStandardIngredient_ProteinFormulation]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[ProteinFormulationStandardIngredient]  WITH CHECK ADD  CONSTRAINT [FK_ProteinFormulationStandardIngredient_ProteinFormulation] FOREIGN KEY([ProteinFormulationID])
REFERENCES [dbo].[ProteinFormulation] ([ID])
GO
ALTER TABLE [dbo].[ProteinFormulationStandardIngredient] CHECK CONSTRAINT [FK_ProteinFormulationStandardIngredient_ProteinFormulation]
GO
/****** Object:  ForeignKey [FK_ProteinLayer_Layer]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[ProteinLayer]  WITH CHECK ADD  CONSTRAINT [FK_ProteinLayer_Layer] FOREIGN KEY([LayerID])
REFERENCES [dbo].[Layer] ([ID])
GO
ALTER TABLE [dbo].[ProteinLayer] CHECK CONSTRAINT [FK_ProteinLayer_Layer]
GO
/****** Object:  ForeignKey [FK_ProteinLayerProteinFormulation_ProteinFormulation]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[ProteinLayerProteinFormulation]  WITH CHECK ADD  CONSTRAINT [FK_ProteinLayerProteinFormulation_ProteinFormulation] FOREIGN KEY([ProteinFormulationID])
REFERENCES [dbo].[ProteinFormulation] ([ID])
GO
ALTER TABLE [dbo].[ProteinLayerProteinFormulation] CHECK CONSTRAINT [FK_ProteinLayerProteinFormulation_ProteinFormulation]
GO
/****** Object:  ForeignKey [FK_ProteinLayerProteinFormulation_ProteinLayer]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[ProteinLayerProteinFormulation]  WITH CHECK ADD  CONSTRAINT [FK_ProteinLayerProteinFormulation_ProteinLayer] FOREIGN KEY([ProteinLayerID])
REFERENCES [dbo].[ProteinLayer] ([ID])
GO
ALTER TABLE [dbo].[ProteinLayerProteinFormulation] CHECK CONSTRAINT [FK_ProteinLayerProteinFormulation_ProteinLayer]
GO
/****** Object:  ForeignKey [FK_Rack_RackType]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[Rack]  WITH CHECK ADD  CONSTRAINT [FK_Rack_RackType] FOREIGN KEY([RackTypeID])
REFERENCES [dbo].[RackType] ([ID])
GO
ALTER TABLE [dbo].[Rack] CHECK CONSTRAINT [FK_Rack_RackType]
GO
/****** Object:  ForeignKey [FK_RackIngredient_IngredientStock]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[RackIngredient]  WITH CHECK ADD  CONSTRAINT [FK_RackIngredient_IngredientStock] FOREIGN KEY([IngredientStockID])
REFERENCES [dbo].[IngredientStock] ([ID])
GO
ALTER TABLE [dbo].[RackIngredient] CHECK CONSTRAINT [FK_RackIngredient_IngredientStock]
GO
/****** Object:  ForeignKey [FK_RackIngredient_Rack]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[RackIngredient]  WITH CHECK ADD  CONSTRAINT [FK_RackIngredient_Rack] FOREIGN KEY([RackID])
REFERENCES [dbo].[Rack] ([ID])
GO
ALTER TABLE [dbo].[RackIngredient] CHECK CONSTRAINT [FK_RackIngredient_Rack]
GO
/****** Object:  ForeignKey [FK_RackType_DispensingRobot]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[RackType]  WITH CHECK ADD  CONSTRAINT [FK_RackType_DispensingRobot] FOREIGN KEY([DispensingRobotID])
REFERENCES [dbo].[DispensingRobot] ([ID])
GO
ALTER TABLE [dbo].[RackType] CHECK CONSTRAINT [FK_RackType_DispensingRobot]
GO
/****** Object:  ForeignKey [FK_RandomLayer_Experiment]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[RandomLayer]  WITH CHECK ADD  CONSTRAINT [FK_RandomLayer_Experiment] FOREIGN KEY([ExperimentID])
REFERENCES [dbo].[Experiment] ([ID])
GO
ALTER TABLE [dbo].[RandomLayer] CHECK CONSTRAINT [FK_RandomLayer_Experiment]
GO
/****** Object:  ForeignKey [FK_RandomLayer_Layer]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[RandomLayer]  WITH CHECK ADD  CONSTRAINT [FK_RandomLayer_Layer] FOREIGN KEY([LayerID])
REFERENCES [dbo].[Layer] ([ID])
GO
ALTER TABLE [dbo].[RandomLayer] CHECK CONSTRAINT [FK_RandomLayer_Layer]
GO
/****** Object:  ForeignKey [FK_RandomLayerExcludeList_Ingredient1]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[RandomLayerExcludeList]  WITH CHECK ADD  CONSTRAINT [FK_RandomLayerExcludeList_Ingredient1] FOREIGN KEY([Ingredient1ID])
REFERENCES [dbo].[Ingredient] ([ID])
GO
ALTER TABLE [dbo].[RandomLayerExcludeList] CHECK CONSTRAINT [FK_RandomLayerExcludeList_Ingredient1]
GO
/****** Object:  ForeignKey [FK_RandomLayerExcludeList_Ingredient2]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[RandomLayerExcludeList]  WITH CHECK ADD  CONSTRAINT [FK_RandomLayerExcludeList_Ingredient2] FOREIGN KEY([Ingredient2ID])
REFERENCES [dbo].[Ingredient] ([ID])
GO
ALTER TABLE [dbo].[RandomLayerExcludeList] CHECK CONSTRAINT [FK_RandomLayerExcludeList_Ingredient2]
GO
/****** Object:  ForeignKey [FK_RandomLayerExcludeList_RandomLayer]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[RandomLayerExcludeList]  WITH CHECK ADD  CONSTRAINT [FK_RandomLayerExcludeList_RandomLayer] FOREIGN KEY([RandomLayerID])
REFERENCES [dbo].[RandomLayer] ([ID])
GO
ALTER TABLE [dbo].[RandomLayerExcludeList] CHECK CONSTRAINT [FK_RandomLayerExcludeList_RandomLayer]
GO
/****** Object:  ForeignKey [FK_RandomLayerGenerationList_RandomLayerIngredientGroup]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[RandomLayerGenerationList]  WITH CHECK ADD  CONSTRAINT [FK_RandomLayerGenerationList_RandomLayerIngredientGroup] FOREIGN KEY([RandomLayerIngredientGroupID])
REFERENCES [dbo].[RandomLayerIngredientGroup] ([ID])
GO
ALTER TABLE [dbo].[RandomLayerGenerationList] CHECK CONSTRAINT [FK_RandomLayerGenerationList_RandomLayerIngredientGroup]
GO
/****** Object:  ForeignKey [FK_RandomLayerGroupIngredient_IngredientIngredientType]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[RandomLayerGroupIngredient]  WITH CHECK ADD  CONSTRAINT [FK_RandomLayerGroupIngredient_IngredientIngredientType] FOREIGN KEY([IngredientIngredientTypeID])
REFERENCES [dbo].[IngredientIngredientType] ([ID])
GO
ALTER TABLE [dbo].[RandomLayerGroupIngredient] CHECK CONSTRAINT [FK_RandomLayerGroupIngredient_IngredientIngredientType]
GO
/****** Object:  ForeignKey [FK_RandomLayerGroupIngredient_IngredientStock]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[RandomLayerGroupIngredient]  WITH CHECK ADD  CONSTRAINT [FK_RandomLayerGroupIngredient_IngredientStock] FOREIGN KEY([IngredientStockID])
REFERENCES [dbo].[IngredientStock] ([ID])
GO
ALTER TABLE [dbo].[RandomLayerGroupIngredient] CHECK CONSTRAINT [FK_RandomLayerGroupIngredient_IngredientStock]
GO
/****** Object:  ForeignKey [FK_RandomLayerGroupIngredient_IngredientStock_HighPH]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[RandomLayerGroupIngredient]  WITH CHECK ADD  CONSTRAINT [FK_RandomLayerGroupIngredient_IngredientStock_HighPH] FOREIGN KEY([HighPHIngredientStockID])
REFERENCES [dbo].[IngredientStock] ([ID])
GO
ALTER TABLE [dbo].[RandomLayerGroupIngredient] CHECK CONSTRAINT [FK_RandomLayerGroupIngredient_IngredientStock_HighPH]
GO
/****** Object:  ForeignKey [FK_RandomLayerGroupIngredient_RandomLayerIngredientGroup]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[RandomLayerGroupIngredient]  WITH CHECK ADD  CONSTRAINT [FK_RandomLayerGroupIngredient_RandomLayerIngredientGroup] FOREIGN KEY([RandomLayerIngredientGroupID])
REFERENCES [dbo].[RandomLayerIngredientGroup] ([ID])
GO
ALTER TABLE [dbo].[RandomLayerGroupIngredient] CHECK CONSTRAINT [FK_RandomLayerGroupIngredient_RandomLayerIngredientGroup]
GO
/****** Object:  ForeignKey [FK_RandomLayerIngredientGroup_RandomLayer]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[RandomLayerIngredientGroup]  WITH CHECK ADD  CONSTRAINT [FK_RandomLayerIngredientGroup_RandomLayer] FOREIGN KEY([RandomLayerID])
REFERENCES [dbo].[RandomLayer] ([ID])
GO
ALTER TABLE [dbo].[RandomLayerIngredientGroup] CHECK CONSTRAINT [FK_RandomLayerIngredientGroup_RandomLayer]
GO
/****** Object:  ForeignKey [FK_Region_Region]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[Region]  WITH CHECK ADD  CONSTRAINT [FK_Region_Region] FOREIGN KEY([ParentRegionID])
REFERENCES [dbo].[Region] ([ID])
GO
ALTER TABLE [dbo].[Region] CHECK CONSTRAINT [FK_Region_Region]
GO
/****** Object:  ForeignKey [FK_Region_RegionType]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[Region]  WITH CHECK ADD  CONSTRAINT [FK_Region_RegionType] FOREIGN KEY([RegionTypeID])
REFERENCES [dbo].[RegionType] ([ID])
GO
ALTER TABLE [dbo].[Region] CHECK CONSTRAINT [FK_Region_RegionType]
GO
/****** Object:  ForeignKey [FK_RegionAnnotation_Region]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[RegionAnnotation]  WITH CHECK ADD  CONSTRAINT [FK_RegionAnnotation_Region] FOREIGN KEY([RegionID])
REFERENCES [dbo].[Region] ([ID])
GO
ALTER TABLE [dbo].[RegionAnnotation] CHECK CONSTRAINT [FK_RegionAnnotation_Region]
GO
/****** Object:  ForeignKey [FK_RegionAnnotation_RegionAnnotationType]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[RegionAnnotation]  WITH CHECK ADD  CONSTRAINT [FK_RegionAnnotation_RegionAnnotationType] FOREIGN KEY([RegionAnnotationTypeID])
REFERENCES [dbo].[RegionAnnotationType] ([ID])
GO
ALTER TABLE [dbo].[RegionAnnotation] CHECK CONSTRAINT [FK_RegionAnnotation_RegionAnnotationType]
GO
/****** Object:  ForeignKey [FK_RegionCaptureProfile_CaptureProfile]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[RegionCaptureProfile]  WITH CHECK ADD  CONSTRAINT [FK_RegionCaptureProfile_CaptureProfile] FOREIGN KEY([CaptureProfileID])
REFERENCES [dbo].[CaptureProfile] ([ID])
GO
ALTER TABLE [dbo].[RegionCaptureProfile] CHECK CONSTRAINT [FK_RegionCaptureProfile_CaptureProfile]
GO
/****** Object:  ForeignKey [FK_RegionCaptureProfile_Region]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[RegionCaptureProfile]  WITH CHECK ADD  CONSTRAINT [FK_RegionCaptureProfile_Region] FOREIGN KEY([RegionID])
REFERENCES [dbo].[Region] ([ID])
GO
ALTER TABLE [dbo].[RegionCaptureProfile] CHECK CONSTRAINT [FK_RegionCaptureProfile_Region]
GO
/****** Object:  ForeignKey [FK_RegionProperty_Region]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[RegionProperty]  WITH CHECK ADD  CONSTRAINT [FK_RegionProperty_Region] FOREIGN KEY([RegionID])
REFERENCES [dbo].[Region] ([ID])
GO
ALTER TABLE [dbo].[RegionProperty] CHECK CONSTRAINT [FK_RegionProperty_Region]
GO
/****** Object:  ForeignKey [FK_RegionRuler_Region]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[RegionRuler]  WITH CHECK ADD  CONSTRAINT [FK_RegionRuler_Region] FOREIGN KEY([RegionID])
REFERENCES [dbo].[Region] ([ID])
GO
ALTER TABLE [dbo].[RegionRuler] CHECK CONSTRAINT [FK_RegionRuler_Region]
GO
/****** Object:  ForeignKey [FK_RegionScribble_Region]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[RegionScribble]  WITH CHECK ADD  CONSTRAINT [FK_RegionScribble_Region] FOREIGN KEY([RegionID])
REFERENCES [dbo].[Region] ([ID])
GO
ALTER TABLE [dbo].[RegionScribble] CHECK CONSTRAINT [FK_RegionScribble_Region]
GO
/****** Object:  ForeignKey [FK_RegionScribblePoint_RegionScribble]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[RegionScribblePoint]  WITH CHECK ADD  CONSTRAINT [FK_RegionScribblePoint_RegionScribble] FOREIGN KEY([RegionScribbleID])
REFERENCES [dbo].[RegionScribble] ([ID])
GO
ALTER TABLE [dbo].[RegionScribblePoint] CHECK CONSTRAINT [FK_RegionScribblePoint_RegionScribble]
GO
/****** Object:  ForeignKey [FK_ScreenLayer_Experiment]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[ScreenLayer]  WITH CHECK ADD  CONSTRAINT [FK_ScreenLayer_Experiment] FOREIGN KEY([ExperimentID])
REFERENCES [dbo].[Experiment] ([ID])
GO
ALTER TABLE [dbo].[ScreenLayer] CHECK CONSTRAINT [FK_ScreenLayer_Experiment]
GO
/****** Object:  ForeignKey [FK_ScreenLayer_Layer]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[ScreenLayer]  WITH CHECK ADD  CONSTRAINT [FK_ScreenLayer_Layer] FOREIGN KEY([LayerID])
REFERENCES [dbo].[Layer] ([ID])
GO
ALTER TABLE [dbo].[ScreenLayer] CHECK CONSTRAINT [FK_ScreenLayer_Layer]
GO
/****** Object:  ForeignKey [FK_ScreenLayer_ScreenLot]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[ScreenLayer]  WITH CHECK ADD  CONSTRAINT [FK_ScreenLayer_ScreenLot] FOREIGN KEY([ScreenLotID])
REFERENCES [dbo].[ScreenLot] ([ID])
GO
ALTER TABLE [dbo].[ScreenLayer] CHECK CONSTRAINT [FK_ScreenLayer_ScreenLot]
GO
/****** Object:  ForeignKey [FK_ScreenLot_Container]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[ScreenLot]  WITH CHECK ADD  CONSTRAINT [FK_ScreenLot_Container] FOREIGN KEY([ContainerID])
REFERENCES [dbo].[Containers] ([ID])
GO
ALTER TABLE [dbo].[ScreenLot] CHECK CONSTRAINT [FK_ScreenLot_Container]
GO
/****** Object:  ForeignKey [FK_ScreenLot_Experiment]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[ScreenLot]  WITH CHECK ADD  CONSTRAINT [FK_ScreenLot_Experiment] FOREIGN KEY([ExperimentID])
REFERENCES [dbo].[Experiment] ([ID])
GO
ALTER TABLE [dbo].[ScreenLot] CHECK CONSTRAINT [FK_ScreenLot_Experiment]
GO
/****** Object:  ForeignKey [FK_ScreenLotPlate_Plate]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[ScreenLotPlate]  WITH CHECK ADD  CONSTRAINT [FK_ScreenLotPlate_Plate] FOREIGN KEY([PlateID])
REFERENCES [dbo].[Plate] ([ID])
GO
ALTER TABLE [dbo].[ScreenLotPlate] CHECK CONSTRAINT [FK_ScreenLotPlate_Plate]
GO
/****** Object:  ForeignKey [FK_ScreenLotPlate_ScreenLot]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[ScreenLotPlate]  WITH CHECK ADD  CONSTRAINT [FK_ScreenLotPlate_ScreenLot] FOREIGN KEY([ScreenLotID])
REFERENCES [dbo].[ScreenLot] ([ID])
GO
ALTER TABLE [dbo].[ScreenLotPlate] CHECK CONSTRAINT [FK_ScreenLotPlate_ScreenLot]
GO
/****** Object:  ForeignKey [FK_Search_FlattenTarget]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[Search]  WITH CHECK ADD  CONSTRAINT [FK_Search_FlattenTarget] FOREIGN KEY([FlattenTargetID])
REFERENCES [dbo].[SearchTarget] ([ID])
GO
ALTER TABLE [dbo].[Search] CHECK CONSTRAINT [FK_Search_FlattenTarget]
GO
/****** Object:  ForeignKey [FK_Search_SearchConstraintTreeNode]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[Search]  WITH NOCHECK ADD  CONSTRAINT [FK_Search_SearchConstraintTreeNode] FOREIGN KEY([RootSearchConstraintTreeNodeID])
REFERENCES [dbo].[SearchConstraintTreeNode] ([ID])
GO
ALTER TABLE [dbo].[Search] CHECK CONSTRAINT [FK_Search_SearchConstraintTreeNode]
GO
/****** Object:  ForeignKey [FK_Search_SearchTarget]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[Search]  WITH CHECK ADD  CONSTRAINT [FK_Search_SearchTarget] FOREIGN KEY([SearchTargetID])
REFERENCES [dbo].[SearchTarget] ([ID])
GO
ALTER TABLE [dbo].[Search] CHECK CONSTRAINT [FK_Search_SearchTarget]
GO
/****** Object:  ForeignKey [FK_Search_TreeNode]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[Search]  WITH NOCHECK ADD  CONSTRAINT [FK_Search_TreeNode] FOREIGN KEY([TreeNodeID])
REFERENCES [dbo].[TreeNode] ([ID])
GO
ALTER TABLE [dbo].[Search] CHECK CONSTRAINT [FK_Search_TreeNode]
GO
/****** Object:  ForeignKey [FK_SearchConstraint_SearchConstraintTreeNode]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[SearchConstraint]  WITH NOCHECK ADD  CONSTRAINT [FK_SearchConstraint_SearchConstraintTreeNode] FOREIGN KEY([SearchConstraintTreeNodeID])
REFERENCES [dbo].[SearchConstraintTreeNode] ([ID])
GO
ALTER TABLE [dbo].[SearchConstraint] CHECK CONSTRAINT [FK_SearchConstraint_SearchConstraintTreeNode]
GO
/****** Object:  ForeignKey [FK_SearchConstraint_SearchField]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[SearchConstraint]  WITH CHECK ADD  CONSTRAINT [FK_SearchConstraint_SearchField] FOREIGN KEY([SearchFieldID])
REFERENCES [dbo].[SearchField] ([ID])
GO
ALTER TABLE [dbo].[SearchConstraint] CHECK CONSTRAINT [FK_SearchConstraint_SearchField]
GO
/****** Object:  ForeignKey [FK_SearchConstraintTreeNode_ParentSearchConstraintTreeNode]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[SearchConstraintTreeNode]  WITH NOCHECK ADD  CONSTRAINT [FK_SearchConstraintTreeNode_ParentSearchConstraintTreeNode] FOREIGN KEY([ParentID])
REFERENCES [dbo].[SearchConstraintTreeNode] ([ID])
GO
ALTER TABLE [dbo].[SearchConstraintTreeNode] CHECK CONSTRAINT [FK_SearchConstraintTreeNode_ParentSearchConstraintTreeNode]
GO
/****** Object:  ForeignKey [FK_SearchConstraintTreeNode_RootSearchConstraintTreeNode]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[SearchConstraintTreeNode]  WITH NOCHECK ADD  CONSTRAINT [FK_SearchConstraintTreeNode_RootSearchConstraintTreeNode] FOREIGN KEY([RootID])
REFERENCES [dbo].[SearchConstraintTreeNode] ([ID])
GO
ALTER TABLE [dbo].[SearchConstraintTreeNode] CHECK CONSTRAINT [FK_SearchConstraintTreeNode_RootSearchConstraintTreeNode]
GO
/****** Object:  ForeignKey [FK_SearchDisplayField_Search]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[SearchDisplayField]  WITH NOCHECK ADD  CONSTRAINT [FK_SearchDisplayField_Search] FOREIGN KEY([SearchID])
REFERENCES [dbo].[Search] ([ID])
GO
ALTER TABLE [dbo].[SearchDisplayField] CHECK CONSTRAINT [FK_SearchDisplayField_Search]
GO
/****** Object:  ForeignKey [FK_SearchDisplayField_SearchField]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[SearchDisplayField]  WITH CHECK ADD  CONSTRAINT [FK_SearchDisplayField_SearchField] FOREIGN KEY([SearchFieldID])
REFERENCES [dbo].[SearchField] ([ID])
GO
ALTER TABLE [dbo].[SearchDisplayField] CHECK CONSTRAINT [FK_SearchDisplayField_SearchField]
GO
/****** Object:  ForeignKey [FK_SearchField_SearchTarget]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[SearchField]  WITH CHECK ADD  CONSTRAINT [FK_SearchField_SearchTarget] FOREIGN KEY([SearchTargetID])
REFERENCES [dbo].[SearchTarget] ([ID])
GO
ALTER TABLE [dbo].[SearchField] CHECK CONSTRAINT [FK_SearchField_SearchTarget]
GO
/****** Object:  ForeignKey [FK_SearchTargetRelations_SearchTarget]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[SearchTargetRelation]  WITH CHECK ADD  CONSTRAINT [FK_SearchTargetRelations_SearchTarget] FOREIGN KEY([SearchTargetParentID])
REFERENCES [dbo].[SearchTarget] ([ID])
GO
ALTER TABLE [dbo].[SearchTargetRelation] CHECK CONSTRAINT [FK_SearchTargetRelations_SearchTarget]
GO
/****** Object:  ForeignKey [FK_SearchTargetRelations_SearchTarget1]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[SearchTargetRelation]  WITH CHECK ADD  CONSTRAINT [FK_SearchTargetRelations_SearchTarget1] FOREIGN KEY([SearchTargetChildID])
REFERENCES [dbo].[SearchTarget] ([ID])
GO
ALTER TABLE [dbo].[SearchTargetRelation] CHECK CONSTRAINT [FK_SearchTargetRelations_SearchTarget1]
GO
/****** Object:  ForeignKey [FK_TreeNode_TreeNode]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[TreeNode]  WITH NOCHECK ADD  CONSTRAINT [FK_TreeNode_TreeNode] FOREIGN KEY([ParentID])
REFERENCES [dbo].[TreeNode] ([ID])
GO
ALTER TABLE [dbo].[TreeNode] CHECK CONSTRAINT [FK_TreeNode_TreeNode]
GO
/****** Object:  ForeignKey [FK_TreeNodeAccessRight_AccessRight]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[TreeNodeAccessRight]  WITH CHECK ADD  CONSTRAINT [FK_TreeNodeAccessRight_AccessRight] FOREIGN KEY([AccessRightID])
REFERENCES [dbo].[AccessRight] ([ID])
GO
ALTER TABLE [dbo].[TreeNodeAccessRight] CHECK CONSTRAINT [FK_TreeNodeAccessRight_AccessRight]
GO
/****** Object:  ForeignKey [FK_TreeNodeAccessRight_Group]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[TreeNodeAccessRight]  WITH CHECK ADD  CONSTRAINT [FK_TreeNodeAccessRight_Group] FOREIGN KEY([GroupID])
REFERENCES [dbo].[Groups] ([ID])
GO
ALTER TABLE [dbo].[TreeNodeAccessRight] CHECK CONSTRAINT [FK_TreeNodeAccessRight_Group]
GO
/****** Object:  ForeignKey [FK_TreeNodeAccessRight_TreeNode]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[TreeNodeAccessRight]  WITH CHECK ADD  CONSTRAINT [FK_TreeNodeAccessRight_TreeNode] FOREIGN KEY([TreeNodeID])
REFERENCES [dbo].[TreeNode] ([ID])
GO
ALTER TABLE [dbo].[TreeNodeAccessRight] CHECK CONSTRAINT [FK_TreeNodeAccessRight_TreeNode]
GO
/****** Object:  ForeignKey [FK_TreeNodeAccessRight_Users]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[TreeNodeAccessRight]  WITH CHECK ADD  CONSTRAINT [FK_TreeNodeAccessRight_Users] FOREIGN KEY([UserID])
REFERENCES [dbo].[Users] ([ID])
GO
ALTER TABLE [dbo].[TreeNodeAccessRight] CHECK CONSTRAINT [FK_TreeNodeAccessRight_Users]
GO
/****** Object:  ForeignKey [FK_TreeNodeFile_TreeNode]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[TreeNodeFile]  WITH CHECK ADD  CONSTRAINT [FK_TreeNodeFile_TreeNode] FOREIGN KEY([TreeNodeID])
REFERENCES [dbo].[TreeNode] ([ID])
GO
ALTER TABLE [dbo].[TreeNodeFile] CHECK CONSTRAINT [FK_TreeNodeFile_TreeNode]
GO
/****** Object:  ForeignKey [FK_Well_Plate]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[Well]  WITH CHECK ADD  CONSTRAINT [FK_Well_Plate] FOREIGN KEY([PlateID])
REFERENCES [dbo].[Plate] ([ID])
GO
ALTER TABLE [dbo].[Well] CHECK CONSTRAINT [FK_Well_Plate]
GO
/****** Object:  ForeignKey [FK_WellDrop_ProteinFormulation]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[WellDrop]  WITH CHECK ADD  CONSTRAINT [FK_WellDrop_ProteinFormulation] FOREIGN KEY([ProteinFormulationID])
REFERENCES [dbo].[ProteinFormulation] ([ID])
GO
ALTER TABLE [dbo].[WellDrop] CHECK CONSTRAINT [FK_WellDrop_ProteinFormulation]
GO
/****** Object:  ForeignKey [FK_WellDrop_Well]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[WellDrop]  WITH CHECK ADD  CONSTRAINT [FK_WellDrop_Well] FOREIGN KEY([WellID])
REFERENCES [dbo].[Well] ([ID])
GO
ALTER TABLE [dbo].[WellDrop] CHECK CONSTRAINT [FK_WellDrop_Well]
GO
/****** Object:  ForeignKey [FK_WellDropLayer_Layer]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[WellDropLayer]  WITH CHECK ADD  CONSTRAINT [FK_WellDropLayer_Layer] FOREIGN KEY([LayerID])
REFERENCES [dbo].[Layer] ([ID])
GO
ALTER TABLE [dbo].[WellDropLayer] CHECK CONSTRAINT [FK_WellDropLayer_Layer]
GO
/****** Object:  ForeignKey [FK_WellDropLayer_Tmp_WellDrop]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[WellDropLayer]  WITH CHECK ADD  CONSTRAINT [FK_WellDropLayer_Tmp_WellDrop] FOREIGN KEY([WellDropID])
REFERENCES [dbo].[WellDrop] ([ID])
GO
ALTER TABLE [dbo].[WellDropLayer] CHECK CONSTRAINT [FK_WellDropLayer_Tmp_WellDrop]
GO
/****** Object:  ForeignKey [FK_WellDropLayerIngredient_IngredientIngredientType]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[WellDropLayerIngredient]  WITH CHECK ADD  CONSTRAINT [FK_WellDropLayerIngredient_IngredientIngredientType] FOREIGN KEY([IngredientIngredientTypeID])
REFERENCES [dbo].[IngredientIngredientType] ([ID])
GO
ALTER TABLE [dbo].[WellDropLayerIngredient] CHECK CONSTRAINT [FK_WellDropLayerIngredient_IngredientIngredientType]
GO
/****** Object:  ForeignKey [FK_WellDropLayerIngredient_IngredientStock]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[WellDropLayerIngredient]  WITH CHECK ADD  CONSTRAINT [FK_WellDropLayerIngredient_IngredientStock] FOREIGN KEY([IngredientStockID])
REFERENCES [dbo].[IngredientStock] ([ID])
GO
ALTER TABLE [dbo].[WellDropLayerIngredient] CHECK CONSTRAINT [FK_WellDropLayerIngredient_IngredientStock]
GO
/****** Object:  ForeignKey [FK_WellDropLayerIngredient_IngredientStock_HighPH]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[WellDropLayerIngredient]  WITH CHECK ADD  CONSTRAINT [FK_WellDropLayerIngredient_IngredientStock_HighPH] FOREIGN KEY([HighPHIngredientStockID])
REFERENCES [dbo].[IngredientStock] ([ID])
GO
ALTER TABLE [dbo].[WellDropLayerIngredient] CHECK CONSTRAINT [FK_WellDropLayerIngredient_IngredientStock_HighPH]
GO
/****** Object:  ForeignKey [FK_WellDropLayerIngredient_WellDropLayer]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[WellDropLayerIngredient]  WITH CHECK ADD  CONSTRAINT [FK_WellDropLayerIngredient_WellDropLayer] FOREIGN KEY([WellDropLayerID])
REFERENCES [dbo].[WellDropLayer] ([ID])
GO
ALTER TABLE [dbo].[WellDropLayerIngredient] CHECK CONSTRAINT [FK_WellDropLayerIngredient_WellDropLayer]
GO
/****** Object:  ForeignKey [FK_WellDropScore_WellDropScoreGroup]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[WellDropScore]  WITH NOCHECK ADD  CONSTRAINT [FK_WellDropScore_WellDropScoreGroup] FOREIGN KEY([WellDropScoreGroupID])
REFERENCES [dbo].[WellDropScoreGroup] ([ID])
GO
ALTER TABLE [dbo].[WellDropScore] CHECK CONSTRAINT [FK_WellDropScore_WellDropScoreGroup]
GO
/****** Object:  ForeignKey [FK_WellLayer_Layer]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[WellLayer]  WITH CHECK ADD  CONSTRAINT [FK_WellLayer_Layer] FOREIGN KEY([LayerID])
REFERENCES [dbo].[Layer] ([ID])
GO
ALTER TABLE [dbo].[WellLayer] CHECK CONSTRAINT [FK_WellLayer_Layer]
GO
/****** Object:  ForeignKey [FK_WellLayer_Well]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[WellLayer]  WITH CHECK ADD  CONSTRAINT [FK_WellLayer_Well] FOREIGN KEY([WellID])
REFERENCES [dbo].[Well] ([ID])
GO
ALTER TABLE [dbo].[WellLayer] CHECK CONSTRAINT [FK_WellLayer_Well]
GO
/****** Object:  ForeignKey [FK_WellLayerIngredient_IngredientIngredientType]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[WellLayerIngredient]  WITH CHECK ADD  CONSTRAINT [FK_WellLayerIngredient_IngredientIngredientType] FOREIGN KEY([IngredientIngredientTypeID])
REFERENCES [dbo].[IngredientIngredientType] ([ID])
GO
ALTER TABLE [dbo].[WellLayerIngredient] CHECK CONSTRAINT [FK_WellLayerIngredient_IngredientIngredientType]
GO
/****** Object:  ForeignKey [FK_WellLayerIngredient_IngredientStock]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[WellLayerIngredient]  WITH CHECK ADD  CONSTRAINT [FK_WellLayerIngredient_IngredientStock] FOREIGN KEY([IngredientStockID])
REFERENCES [dbo].[IngredientStock] ([ID])
GO
ALTER TABLE [dbo].[WellLayerIngredient] CHECK CONSTRAINT [FK_WellLayerIngredient_IngredientStock]
GO
/****** Object:  ForeignKey [FK_WellLayerIngredient_IngredientStock_HighPH]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[WellLayerIngredient]  WITH CHECK ADD  CONSTRAINT [FK_WellLayerIngredient_IngredientStock_HighPH] FOREIGN KEY([HighPHIngredientStockID])
REFERENCES [dbo].[IngredientStock] ([ID])
GO
ALTER TABLE [dbo].[WellLayerIngredient] CHECK CONSTRAINT [FK_WellLayerIngredient_IngredientStock_HighPH]
GO
/****** Object:  ForeignKey [FK_WellLayerIngredient_WellLayer]    Script Date: 01/19/2012 13:13:28 ******/
ALTER TABLE [dbo].[WellLayerIngredient]  WITH CHECK ADD  CONSTRAINT [FK_WellLayerIngredient_WellLayer] FOREIGN KEY([WellLayerID])
REFERENCES [dbo].[WellLayer] ([ID])
GO
ALTER TABLE [dbo].[WellLayerIngredient] CHECK CONSTRAINT [FK_WellLayerIngredient_WellLayer]
GO
