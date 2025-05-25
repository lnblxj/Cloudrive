/*
 Navicat Premium Dump SQL

 Source Server         : Mysql
 Source Server Type    : MySQL
 Source Server Version : 80404 (8.4.4)
 Source Host           : 192.168.0.183:3306
 Source Schema         : cloudrive

 Target Server Type    : MySQL
 Target Server Version : 80404 (8.4.4)
 File Encoding         : 65001

 Date: 08/05/2025 09:15:38
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for cd_blog_01
-- ----------------------------
DROP TABLE IF EXISTS `cd_blog_01`;
CREATE TABLE `cd_blog_01`  (
  `id` bigint NOT NULL COMMENT 'id',
  `title` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '标题',
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '正文内容',
  `summary` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '摘要',
  `category_id` bigint NOT NULL COMMENT '分类',
  `thumbnail` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '缩略图',
  `view_count` bigint NOT NULL DEFAULT 0 COMMENT '浏览量',
  `comment_count` bigint NOT NULL DEFAULT 0 COMMENT '评论量',
  `create_by` bigint NOT NULL COMMENT '作者',
  `create_time` timestamp NOT NULL COMMENT '创建时间',
  `update_by` bigint NOT NULL COMMENT '更新者',
  `update_time` timestamp NOT NULL COMMENT '更新时间',
  `status` int NOT NULL COMMENT '状态：0=审核中 1=正常 2=下架',
  `del_flag` int NOT NULL DEFAULT 0 COMMENT '是否删除0=否',
  `is_markdown` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否是markdown',
  `image_urls` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '配图地址，JSON格式',
  `share_link` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '分享链接',
  `need_password` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否需要提取密码',
  `password` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '提取密码',
  `tags` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '自定义标签，JSON格式',
  `top_tag` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '顶级标签',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '文章表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of cd_blog_01
-- ----------------------------
INSERT INTO `cd_blog_01` VALUES (1918100874399002624, 'breeze-shell v0.1.22 Win10/11右键菜单美化增强工具', 'breeze-shell是一个次世代的右键菜单解决方案，旨在为Windows用户重新带来流畅与精致的操作体验。它采用现代化的设计理念，通过交互动画和无限扩展的可能性，让右键菜单变得更加智能和个性化。基于自研 breeze-ui 框架实现，这是一个跨平台、简洁优雅、动画友好的现代 C++ UI 库，支持 NanoVG 和 ThorVG 双渲染后端。这使得软件能在约 2MB 的体积下实现精致的视觉体验。', 'breeze-shell是一个次世代的右键菜单解决方案，旨在为Windows用户重新带来流畅与精致的操作体验。它采用现代化的设计理念，通过交互动画和无限扩展的可能性，让右键菜单变得更加智能和个性化。基', 4, 'http://192.168.0.183:9000/graphs/778aa5e2-3dae-481c-9e87-1270f9ff1901.jpg', 1, 0, 1918099596923035648, '2025-05-02 08:31:00', 1, '2025-05-02 08:32:02', 1, 0, 0, '[\"http://192.168.0.183:9000/graphs/778aa5e2-3dae-481c-9e87-1270f9ff1901.jpg\"]', 'https://pan.baidu.com/share/init?surl=23xYk6tFv_hIxJXybt41CA', 1, '6666', '[\"软件\",\"工具\"]', 'normal');
INSERT INTO `cd_blog_01` VALUES (1918110864778186752, 'Seelen UI v2.3.0 开源免费桌面美化工具', 'Seelen UI是一款基于Web的桌面环境，旨在为win10/11系统用户提供类似MacOS的桌面风格，同时保持Windows系统的核心功能。它不仅支持高度自定义，包括主题、Dock、工具栏等，还通过其高级的定制功能和创新的窗口平铺管理器，让用户的工作效率和个性化体验得到显著提升。Seelen UI是一个旨在增强您的Windows桌面体验的工具，专注于定制和生产力。它平滑地集成到您的系统中，提供了一系列功能，使您能够个性化桌面并优化工作流程。\n\n软件特色\n实时预览：Seelen UI支持实时预览功能，用户在设计过程中可以随时查看UI效果，无需等待渲染，大大提高了设计效率。\n高度自定义：软件提供了丰富的设计元素和模板，同时支持用户自定义颜色、字体、图标等，满足个性化设计需求。\n跨平台兼容：Seelen UI设计的UI界面能够完美适配多种操作系统和设备，确保用户在不同平台上都能获得一致且优质的体验。', 'Seelen UI是一款基于Web的桌面环境，旨在为win10/11系统用户提供类似MacOS的桌面风格，同时保持Windows系统的核心功能。它不仅支持高度自定义，包括主题、Dock、工具栏等，还通', 4, 'http://192.168.0.183:9000/graphs/ac815352-51c9-4e1f-a141-4880f1f8448f.png', 1, 0, 1918099596923035648, '2025-05-02 09:10:42', 1, '2025-05-02 09:10:57', 1, 0, 0, '[\"http://192.168.0.183:9000/graphs/ac815352-51c9-4e1f-a141-4880f1f8448f.png\",\"http://192.168.0.183:9000/graphs/df47fe17-3a17-4bc3-a40b-b1f10a0af027.png\"]', 'https://pan.quark.cn/s/448334f3f376#/list/share', 0, '', '[\"美化\",\"软件\",\"win\"]', 'normal');
INSERT INTO `cd_blog_01` VALUES (1918111789374754816, '【无损音乐】许嵩 - 寻雾启示 (2009) ALAC', '名称：许嵩 - 寻雾启示 (2009) ALAC\n\n\n\n描述：《寻雾启示》是许嵩的第二张个人创作专辑，于2010年1月6日发行。这张专辑延续了许嵩独特的音乐风格，以深刻的歌词和优美的旋律赢得了广泛好评。专辑名称“寻雾启示”寓意着在迷雾中寻找真相和方向，反映了许嵩对生活、情感和社会的思考。\n\n\n\n专辑曲目：\n\n1. 叹服 \n\n2. 灰色头像  \n\n3. 我无所谓 \n\n4. 庐州月\n\n5. 不煽情  \n\n6. 我们的恋爱是对生命的严重浪费\n\n7. 白马非马 \n\n8. 单人旅途\n\n9. 在那不遥远的地方', '名称：许嵩 - 寻雾启示 (2009) ALAC\n\n\n\n描述：《寻雾启示》是许嵩的第二张个人创作专辑，于2010年1月6日发行。这张专辑延续了许嵩独特的音乐风格，以深刻的歌词和优美的旋律赢得了广泛好评', 6, 'http://192.168.0.183:9000/graphs/1bee53e9-8940-44e1-a36e-aaa3498bcd4e.jpg', 6, 0, 1918099596923035648, '2025-05-02 09:14:22', 1, '2025-05-06 18:41:29', 1, 0, 0, '[\"http://192.168.0.183:9000/graphs/1bee53e9-8940-44e1-a36e-aaa3498bcd4e.jpg\"]', 'https://cloud.189.cn/t/qyyQjaQNfIJb', 0, '', '[\"许嵩\",\"音乐\",\"国语\"]', 'normal');
INSERT INTO `cd_blog_01` VALUES (1918112925259386880, '《Beyond-专辑系列》[WAV+CUE][257.42G]', 'BEYOND是一种精神，是一种不朽的精神。\n\n 当第一次听到BEYOND的声音，心灵里回荡的是一种源远流长的骨气和正义。因为他们都是中国人，我想这是我喜欢他们的音乐的原因。虽然广东的语言使他们的歌曲似乎有点不能广泛流传，但是一种共同的感觉是铿锵和渴望。家驹是BEYOND的灵魂，他的作品让人惊叹和共鸣。 我们从《海阔天空》得到解放，从《喜欢你》释放柔情，从《冷雨夜》忧郁自己，从《旧日足迹》缅怀从前，每一个BEYONG的追随者都能从家驹苍而遒劲的声音找到很多需要的东西。虽然家驹已经不在，但他的歌声和作品是不朽的，至少在中国流行乐坛。 如果说没有听过BEYONG的歌曲，我认为就没有了解香港乐坛，就没有了解亚洲音乐.', 'BEYOND是一种精神，是一种不朽的精神。\n\n 当第一次听到BEYOND的声音，心灵里回荡的是一种源远流长的骨气和正义。因为他们都是中国人，我想这是我喜欢他们的音乐的原因。虽然广东的语言使他们的歌曲似', 6, 'http://192.168.0.183:9000/graphs/8f551ac9-3141-42f4-a933-349a4c45a545.jpg', 1, 0, 1918099596923035648, '2025-05-02 09:18:53', 1, '2025-05-02 09:19:57', 1, 0, 0, '[\"http://192.168.0.183:9000/graphs/8f551ac9-3141-42f4-a933-349a4c45a545.jpg\"]', 'https://cloud.189.cn/t/n2eaYr7NbYRf', 1, 'm8hy', '[\"专辑\",\"音乐\",\"典藏\"]', 'normal');
INSERT INTO `cd_blog_01` VALUES (1918113826560790528, '飓风营救', '《飓风营救》（英语：Taken，或记作96 Hours和The Hostage）是一部2008年英语法国动作惊悚片，由皮埃尔·莫瑞尔执导，吕克·贝松监制并与罗伯特·马克·卡门共同担任编剧，连姆·尼森、玛姬·格蕾斯、法姆克·扬森、凯蒂·卡西迪、勒兰德·奥瑟、琼·葛兹、大卫·华沙斯基、霍莉·瓦兰丝和山德·贝克利主演。电影讲述前中央情报局探员布莱恩·米尔斯（Bryan Mills，尼逊饰演）的女儿及好友在法国度假时被阿尔巴尼亚人贩子绑架，使布莱恩飞往巴黎循线追击目标的下落。', '《飓风营救》（英语：Taken，或记作96 Hours和The Hostage）是一部2008年英语法国动作惊悚片，由皮埃尔·莫瑞尔执导，吕克·贝松监制并与罗伯特·马克·卡门共同担任编剧，连姆·尼森、', 3, 'http://192.168.0.183:9000/graphs/3b9f7d4f-dea5-40ef-b624-7842417a8278.jpg', 0, 0, 1918099596923035648, '2025-05-02 09:22:28', 1918099596923035648, '2025-05-02 09:22:28', 1, 0, 0, '[\"http://192.168.0.183:9000/graphs/3b9f7d4f-dea5-40ef-b624-7842417a8278.jpg\"]', 'https://cloud.189.cn/t/aiYNZjfQnEZj', 0, '', '[\"电影\",\"繁简\"]', 'normal');
INSERT INTO `cd_blog_01` VALUES (1918114503257214976, '环球国家地理百科全书(套装共10册)', '《环球国家地理百科全书(套装共10册)》包括《亚洲1》《亚洲2》《亚洲3》《亚洲、大洋洲》《欧洲1》《欧洲2》《欧洲3》《非洲》《北美洲》《南北洲、两极地区》共10册。《环球国家地理百科全书(套装共10册)》是一套色彩艳丽、内容丰富、特色鲜明的大众地理读物。', '《环球国家地理百科全书(套装共10册)》包括《亚洲1》《亚洲2》《亚洲3》《亚洲、大洋洲》《欧洲1》《欧洲2》《欧洲3》《非洲》《北美洲》《南北洲、两极地区》共10册。《环球国家地理百科全书(套装共1', 7, 'http://192.168.0.183:9000/graphs/4338a8f2-3d6c-42e4-967e-1a3fb6b5471c.png', 0, 0, 1918099596923035648, '2025-05-02 09:25:09', 1918099596923035648, '2025-05-02 09:25:09', 1, 0, 0, '[\"http://192.168.0.183:9000/graphs/4338a8f2-3d6c-42e4-967e-1a3fb6b5471c.png\"]', 'https://cloud.189.cn/t/7RbAFfmaMVna', 0, '', '[\"书籍\",\"地理\",\"王越\"]', 'normal');
INSERT INTO `cd_blog_01` VALUES (1918115231048650752, '数学可以这样学：专业又有趣的数学科普大集合（套装共9册）', '## 作者\n史蒂芬·霍金、杰森·威尔克斯、陈卓、约翰·查尔顿·波金霍尔、吉姆·亨勒、列纳德·蒙洛迪诺、马克·钱伯兰、赫尔曼·外尔、约瑟夫·马祖尔\n\n## 内容简介\n本套装集结了《上帝创造整数》《烧掉数学书》《离散的世界》《数学的意义》《证明与布丁》《欧几里得之窗》《数字乾坤》《对称》《巧合》9本数学科普著作，作者系史蒂芬·霍金、约翰·查尔顿·波金霍尔、列纳德·蒙洛迪诺、马克·钱伯兰、赫尔曼·外尔等大名鼎鼎的数学学者和专家。', '作者\n史蒂芬·霍金、杰森·威尔克斯、陈卓、约翰·查尔顿·波金霍尔、吉姆·亨勒、列纳德·蒙洛迪诺、马克·钱伯兰、赫尔曼·外尔、约瑟夫·马祖尔\n\n内容简介\n本套装集结了《上帝创造整数》《烧掉数学书》《离散', 7, 'http://192.168.0.183:9000/graphs/7cde74b8-a45a-4a6b-be35-e256774450e3.jpg', 1, 0, 1918099596923035648, '2025-05-02 09:28:03', 1, '2025-05-02 09:28:57', 1, 0, 1, '[\"http://192.168.0.183:9000/graphs/7cde74b8-a45a-4a6b-be35-e256774450e3.jpg\"]', 'https://cloud.189.cn/t/bmMzQfVrAVzm', 0, '', '[\"数学\",\"书籍\"]', 'normal');
INSERT INTO `cd_blog_01` VALUES (1918116260494430208, '黑马程序员Java项目《苍穹外卖》企业级开发实战 附教程视频 项目实例 工程文件 13.3 GB', '黑马程序员Java项目《苍穹外卖》企业级开发实战 附教程视频 项目实例 工程文件 13.3 GB\n\n本项目（苍穹外卖）是专门为餐饮企业（餐厅、饭店）定制的一款软件产品，包括 系统管理后台 和 小程序端应用 两部分。其中系统管理后台主要提供给餐饮企业内部员工使用，可以对餐厅的分类、菜品、套餐、订单、员工等进行管理维护，对餐厅的各类数据进行统计，同时也可进行来单语音播报功能。小程序端主要提供给消费者使用，可以在线浏览菜品、添加购物车、下单、支付、催单等。', '黑马程序员Java项目《苍穹外卖》企业级开发实战 附教程视频 项目实例 工程文件 13.3 GB\n\n本项目（苍穹外卖）是专门为餐饮企业（餐厅、饭店）定制的一款软件产品，包括 系统管理后台 和 小程序端', 5, 'http://192.168.0.183:9000/graphs/18b5be93-b23b-45e6-bf82-a108d2d16ab1.jpg', 1, 0, 1918099596923035648, '2025-05-02 09:32:08', 1, '2025-05-05 18:34:41', 1, 0, 0, '[\"http://192.168.0.183:9000/graphs/18b5be93-b23b-45e6-bf82-a108d2d16ab1.jpg\",\"http://192.168.0.183:9000/graphs/3595fce7-acbd-431b-94ef-9b39c4e52b84.png\"]', 'http://cloud.189.cn/t/2MnaYfveieAn', 0, '', '[\"黑马\",\"编程\"]', 'normal');
INSERT INTO `cd_blog_01` VALUES (1918119461218144256, '影视飓风免费空镜素材 82.68GB', '影视飓风免费空镜素材 82.68GB', '影视飓风免费空镜素材 82.68GB', 2, 'http://192.168.0.183:9000/graphs/7a9ff08d-d97e-467a-abd5-0dbca8d46466.png', 3, 3, 1918099596923035648, '2025-05-02 09:44:51', 1, '2025-05-02 10:31:58', 1, 0, 0, '[\"http://192.168.0.183:9000/graphs/7a9ff08d-d97e-467a-abd5-0dbca8d46466.png\"]', 'https://cloud.189.cn/t/aMJ3YzZZz2ua', 0, '', '[\"素材\",\"视频\"]', 'popular');
INSERT INTO `cd_blog_01` VALUES (1918121360453521408, '壁纸分享', '空调、藤曼、向日葵', '空调、藤曼、向日葵', 8, 'http://192.168.0.183:9000/graphs/7dd8182f-6331-4e74-aee9-3d2da2e762b4.jpg', 5, 1, 1918099596923035648, '2025-05-02 09:52:24', 1, '2025-05-06 18:47:29', 1, 0, 0, '[\"http://192.168.0.183:9000/graphs/7dd8182f-6331-4e74-aee9-3d2da2e762b4.jpg\"]', 'http://cloudrive.sboxm.top/#/pages/share/share?id=1918120935255953408', 0, '', '[\"图片\",\"壁纸\"]', 'normal');
INSERT INTO `cd_blog_01` VALUES (1918124937418887168, '五一劳动节 PPT 模板 293套 5.48G', '五一劳动节 PPT 模板 293套 5.48G \\n 五一劳动节，又称国际劳动节，定于每年的5月1日。\\n它是全世界劳动人民共同拥有的节日，起源于1886年美国芝加哥的工人大罢工。\\n为纪念这次伟大的工人运动，1889年7月，在恩格斯组织召开的第二国际成立大会上宣布将每年的5月1日定为国际劳动节。\\n中国于1949年12月将五一劳动节定为法定节假日，全国放假一天。\\n劳动节的意义在于劳动者通过斗争，用顽强、英勇不屈的奋斗精神，争取到了自己的合法权益。\\n如今，五一劳动节已成为中国重要的黄金周假期之一，人们常利用这个假期出游或休息。\\n劳动节也是表彰劳动者贡献、弘扬劳动精神的重要时刻。', '五一劳动节 PPT 模板 293套 5.48G', 1, 'http://192.168.0.183:9000/graphs/f67ded61-0b4d-4597-ab9a-6690e5258402.png', 36, 4, 1918099596923035648, '2025-05-02 10:06:37', 1, '2025-05-06 18:47:30', 1, 0, 0, '[\"http://192.168.0.183:9000/graphs/f67ded61-0b4d-4597-ab9a-6690e5258402.png\"]', 'https://cloud.189.cn/t/aIb2IvUNRBni', 0, '', '[\"PPT\",\"劳动节\"]', 'selected');

-- ----------------------------
-- Table structure for cd_blog_report
-- ----------------------------
DROP TABLE IF EXISTS `cd_blog_report`;
CREATE TABLE `cd_blog_report`  (
  `id` bigint NOT NULL COMMENT '举报记录ID',
  `blog_id` bigint NOT NULL COMMENT '被举报文章ID',
  `report_user` bigint NOT NULL COMMENT '举报人ID',
  `report_type` int NOT NULL COMMENT '举报类型：0=垃圾广告 1=违规内容 2=侵权 3=其他',
  `report_content` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '举报详情',
  `evidence` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '证据材料(JSON格式)',
  `report_time` timestamp NOT NULL COMMENT '举报时间',
  `handler` bigint NULL DEFAULT NULL COMMENT '处理人ID',
  `handle_time` timestamp NULL DEFAULT NULL COMMENT '处理时间',
  `status` int NOT NULL DEFAULT 0 COMMENT '状态：0=待处理 1=已处理 2=已驳回',
  `handle_remark` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '处理备注',
  `del_flag` int NOT NULL DEFAULT 0 COMMENT '删除标志：0=有效 1=删除',
  `create_time` timestamp NOT NULL COMMENT '创建时间',
  `update_time` timestamp NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_blog`(`blog_id` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '文章举报信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of cd_blog_report
-- ----------------------------

-- ----------------------------
-- Table structure for cd_capacity_info
-- ----------------------------
DROP TABLE IF EXISTS `cd_capacity_info`;
CREATE TABLE `cd_capacity_info`  (
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `total_capacity` bigint NOT NULL COMMENT '总容量（字节）',
  `used_capacity` bigint NOT NULL COMMENT '已用容量（字节）',
  `create_time` timestamp NOT NULL COMMENT '创建时间',
  `update_time` timestamp NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '容量信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of cd_capacity_info
-- ----------------------------
INSERT INTO `cd_capacity_info` VALUES (1918099596923035648, 10737418240, 1427053, '2025-05-02 08:25:55', '2025-05-06 18:42:54');
INSERT INTO `cd_capacity_info` VALUES (1918127834806280192, 10737418240, 0, '2025-05-02 10:18:08', '2025-05-02 10:18:08');
INSERT INTO `cd_capacity_info` VALUES (1918130336226271232, 10737418240, 0, '2025-05-02 10:28:04', '2025-05-02 10:28:04');

-- ----------------------------
-- Table structure for cd_comment
-- ----------------------------
DROP TABLE IF EXISTS `cd_comment`;
CREATE TABLE `cd_comment`  (
  `id` bigint NOT NULL COMMENT 'id',
  `article_id` bigint NOT NULL COMMENT '所属文章',
  `content` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '评论内容',
  `create_by` bigint NOT NULL COMMENT '创建者',
  `create_time` timestamp NOT NULL COMMENT '创建时间',
  `nick_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户昵称',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户头像',
  `update_by` bigint NOT NULL COMMENT '修改人',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '修改时间',
  `del_flag` int NOT NULL DEFAULT 0 COMMENT '是否删除0=否1=是',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '评论表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of cd_comment
-- ----------------------------
INSERT INTO `cd_comment` VALUES (1918127499392045056, 1918124937418887168, 'PPT模板来啦', 1918099596923035648, '2025-05-02 10:16:48', '北冥有鱼', 'http://cdnjson.com/images/2025/04/16/logo-light.png', 1918099596923035648, '2025-05-02 10:16:48', 0);
INSERT INTO `cd_comment` VALUES (1918128061936291840, 1918124937418887168, '感谢分享！', 1918127834806280192, '2025-05-02 10:19:02', '坤坤', 'http://192.168.0.183:9000/graphs/658532e7-d93a-4f8c-8af6-b3f41d2c4ffc.png', 1918127834806280192, '2025-05-02 10:19:02', 0);
INSERT INTO `cd_comment` VALUES (1918128681078476800, 1918119461218144256, '感谢大佬们整理分享', 1918127834806280192, '2025-05-02 10:21:29', '坤坤', 'http://192.168.0.183:9000/graphs/658532e7-d93a-4f8c-8af6-b3f41d2c4ffc.png', 1918127834806280192, '2025-05-02 10:21:29', 0);
INSERT INTO `cd_comment` VALUES (1918130846794764288, 1918124937418887168, '66666', 1918130336226271232, '2025-05-02 10:30:06', '不柿戈们', 'http://192.168.0.183:9000/graphs/06ef7781-dbfb-438e-ad35-f031f736276c.png', 1918130336226271232, '2025-05-02 10:30:06', 0);
INSERT INTO `cd_comment` VALUES (1918130885491412992, 1918119461218144256, '6666', 1918130336226271232, '2025-05-02 10:30:15', '不柿戈们', 'http://192.168.0.183:9000/graphs/06ef7781-dbfb-438e-ad35-f031f736276c.png', 1918130336226271232, '2025-05-02 10:30:15', 0);
INSERT INTO `cd_comment` VALUES (1918130951136464896, 1918119461218144256, '大佬有没有壁纸的素材？', 1918130336226271232, '2025-05-02 10:30:31', '不柿戈们', 'http://192.168.0.183:9000/graphs/06ef7781-dbfb-438e-ad35-f031f736276c.png', 1918130336226271232, '2025-05-02 10:30:31', 0);
INSERT INTO `cd_comment` VALUES (1918131050017181696, 1918121360453521408, 'ddd, 拿走了，感谢感谢', 1918130336226271232, '2025-05-02 10:30:54', '不柿戈们', 'http://192.168.0.183:9000/graphs/06ef7781-dbfb-438e-ad35-f031f736276c.png', 1918130336226271232, '2025-05-02 10:30:54', 0);
INSERT INTO `cd_comment` VALUES (1919606035329404928, 1918124937418887168, '你好你好', 1918099596923035648, '2025-05-06 12:11:58', '北冥有鱼', 'http://cdnjson.com/images/2025/04/16/logo-light.png', 1918099596923035648, '2025-05-06 12:11:58', 0);
INSERT INTO `cd_comment` VALUES (1919705596293787648, 1918124937418887168, '评论一下', 1918127834806280192, '2025-05-06 18:47:35', '坤坤', 'http://192.168.0.183:9000/graphs/658532e7-d93a-4f8c-8af6-b3f41d2c4ffc.png', 1918127834806280192, '2025-05-06 18:47:35', 0);

-- ----------------------------
-- Table structure for cd_file_share
-- ----------------------------
DROP TABLE IF EXISTS `cd_file_share`;
CREATE TABLE `cd_file_share`  (
  `id` bigint NOT NULL COMMENT 'id',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `nick_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户昵称',
  `file_name` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '文件名称',
  `file_size` bigint NOT NULL COMMENT '文件大小（字节）',
  `need_password` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否需要提取密码：0=不需要 1=需要',
  `password` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '提取密码',
  `minio_object_name` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'Minio对象名称',
  `create_by` bigint NOT NULL COMMENT '创建者',
  `create_time` timestamp NOT NULL COMMENT '创建时间',
  `update_by` bigint NOT NULL COMMENT '更新者',
  `update_time` timestamp NOT NULL COMMENT '更新时间',
  `status` int NOT NULL COMMENT '状态：0=审核中 1=正常 2=下架',
  `del_flag` int NOT NULL DEFAULT 0 COMMENT '是否删除0=否',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '文件分享表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of cd_file_share
-- ----------------------------
INSERT INTO `cd_file_share` VALUES (1918120935255953408, 1918099596923035648, '北冥有鱼', '67ae26324d27bdef7b551fc3181979c7--1424009633.jpg', 1413337, 0, NULL, '137a8f5d5a0036b558e5007431a4920c78c580c48c21c0ccb0f84f29ab58a70d', 1918099596923035648, '2025-05-02 09:50:43', 1918099596923035648, '2025-05-02 09:50:43', 1, 0);

-- ----------------------------
-- Table structure for cd_image_bed
-- ----------------------------
DROP TABLE IF EXISTS `cd_image_bed`;
CREATE TABLE `cd_image_bed`  (
  `id` bigint NOT NULL COMMENT '主键ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `minio_object_name` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'MinIO对象名称',
  `create_time` timestamp NOT NULL COMMENT '创建时间',
  `del_flag` int NOT NULL DEFAULT 0 COMMENT '是否删除（0=未删除，1=已删除）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '图床表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of cd_image_bed
-- ----------------------------
INSERT INTO `cd_image_bed` VALUES (1918100556600782848, 1918099596923035648, '778aa5e2-3dae-481c-9e87-1270f9ff1901.jpg', '2025-05-02 08:29:44', 0);
INSERT INTO `cd_image_bed` VALUES (1918110549492314112, 1918099596923035648, 'ac815352-51c9-4e1f-a141-4880f1f8448f.png', '2025-05-02 09:09:26', 0);
INSERT INTO `cd_image_bed` VALUES (1918110561018261504, 1918099596923035648, 'df47fe17-3a17-4bc3-a40b-b1f10a0af027.png', '2025-05-02 09:09:29', 0);
INSERT INTO `cd_image_bed` VALUES (1918111555798118400, 1918099596923035648, '1bee53e9-8940-44e1-a36e-aaa3498bcd4e.jpg', '2025-05-02 09:13:26', 0);
INSERT INTO `cd_image_bed` VALUES (1918112642861051904, 1918099596923035648, '8f551ac9-3141-42f4-a933-349a4c45a545.jpg', '2025-05-02 09:17:46', 0);
INSERT INTO `cd_image_bed` VALUES (1918113475539447808, 1918099596923035648, '3b9f7d4f-dea5-40ef-b624-7842417a8278.jpg', '2025-05-02 09:21:04', 0);
INSERT INTO `cd_image_bed` VALUES (1918114275678433280, 1918099596923035648, '4338a8f2-3d6c-42e4-967e-1a3fb6b5471c.png', '2025-05-02 09:24:15', 0);
INSERT INTO `cd_image_bed` VALUES (1918114850054811648, 1918099596923035648, '7cde74b8-a45a-4a6b-be35-e256774450e3.jpg', '2025-05-02 09:26:32', 0);
INSERT INTO `cd_image_bed` VALUES (1918115785892114432, 1918099596923035648, '18b5be93-b23b-45e6-bf82-a108d2d16ab1.jpg', '2025-05-02 09:30:15', 0);
INSERT INTO `cd_image_bed` VALUES (1918115900614717440, 1918099596923035648, '3595fce7-acbd-431b-94ef-9b39c4e52b84.png', '2025-05-02 09:30:42', 0);
INSERT INTO `cd_image_bed` VALUES (1918119358088556544, 1918099596923035648, '7a9ff08d-d97e-467a-abd5-0dbca8d46466.png', '2025-05-02 09:44:27', 0);
INSERT INTO `cd_image_bed` VALUES (1918123706721894400, 1918099596923035648, '7dd8182f-6331-4e74-aee9-3d2da2e762b4.jpg', '2025-05-02 10:01:43', 0);
INSERT INTO `cd_image_bed` VALUES (1918124741641240576, 1918099596923035648, 'f67ded61-0b4d-4597-ab9a-6690e5258402.png', '2025-05-02 10:05:50', 0);
INSERT INTO `cd_image_bed` VALUES (1918128003270443008, 1918127834806280192, '658532e7-d93a-4f8c-8af6-b3f41d2c4ffc.png', '2025-05-02 10:18:48', 0);
INSERT INTO `cd_image_bed` VALUES (1918130788812587008, 1918130336226271232, '06ef7781-dbfb-438e-ad35-f031f736276c.png', '2025-05-02 10:29:52', 0);
INSERT INTO `cd_image_bed` VALUES (1919606115314790400, 1918099596923035648, 'aeb93a63-0b95-4bcf-a3d3-07eb3b6d1c98.jpg', '2025-05-06 12:12:17', 0);
INSERT INTO `cd_image_bed` VALUES (1919606118959640576, 1918099596923035648, '2bbb8126-54db-4744-a29c-e1c3e4b0b7bb.png', '2025-05-06 12:12:18', 0);
INSERT INTO `cd_image_bed` VALUES (1919607294333644800, 1918099596923035648, '9b28dbba-8e83-4969-845e-ae89b416fcdf.png', '2025-05-06 12:16:58', 0);
INSERT INTO `cd_image_bed` VALUES (1919607325493129216, 1918099596923035648, '433676fe-8523-4ceb-8288-cf25a7f50b33.jpg', '2025-05-06 12:17:06', 0);
INSERT INTO `cd_image_bed` VALUES (1919609060307595264, 1918099596923035648, 'e6e1cf1c-0a51-4fa8-a4fa-2c78e5afc737.png', '2025-05-06 12:23:59', 1);
INSERT INTO `cd_image_bed` VALUES (1919609062857732096, 1918099596923035648, '6553c202-1e34-45f8-bc8d-0fd00f63c5fe.png', '2025-05-06 12:24:00', 1);
INSERT INTO `cd_image_bed` VALUES (1919704575760896000, 1918099596923035648, 'acd995a3-4eaa-4ad5-9be9-7b437f34bfce.png', '2025-05-06 18:43:32', 0);

-- ----------------------------
-- Table structure for cd_user
-- ----------------------------
DROP TABLE IF EXISTS `cd_user`;
CREATE TABLE `cd_user`  (
  `id` bigint NOT NULL COMMENT 'id',
  `email` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '唯一，用户邮箱',
  `nickname` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '唯一，用户昵称',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户头像直链',
  `password` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码',
  `role` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '类型：0=普通用户1=管理员',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '状态：0=正常1=封禁',
  `create_time` timestamp NOT NULL COMMENT '创建时间',
  `create_by` bigint NOT NULL COMMENT '创建人',
  `update_time` timestamp NOT NULL COMMENT '更新时间',
  `update_by` bigint NOT NULL COMMENT '更新人',
  `del_flag` int NOT NULL DEFAULT 0 COMMENT '0=未删除1=已删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `email`(`email` ASC) USING BTREE,
  UNIQUE INDEX `nickname`(`nickname` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of cd_user
-- ----------------------------
INSERT INTO `cd_user` VALUES (1918099596923035648, 'sboxmemail@gmail.com', '北冥有鱼', 'http://192.168.0.183:9000/graphs/acd995a3-4eaa-4ad5-9be9-7b437f34bfce.png', '$2a$10$.XZcWyZR93tKh6B39tu05O/TaRAQ2sZi1w3bg9gaZohX7ANb3dhIu', '0', '0', '2025-05-02 08:25:55', -1, '2025-05-06 18:43:38', 1918099596923035648, 0);
INSERT INTO `cd_user` VALUES (1918127834806280192, 'cnhkemail@foxmail.com', '坤坤', 'http://192.168.0.183:9000/graphs/658532e7-d93a-4f8c-8af6-b3f41d2c4ffc.png', '$2a$10$S9aupmfj8TL3kSrfQdwijOujtQb4K/BvOQP0I42bIrZ2NBexFog8O', '0', '0', '2025-05-02 10:18:08', -1, '2025-05-02 10:18:48', 1918127834806280192, 0);
INSERT INTO `cd_user` VALUES (1918130336226271232, 'i5sdpc3lph@iwatermail.com', '不柿戈们', 'http://192.168.0.183:9000/graphs/06ef7781-dbfb-438e-ad35-f031f736276c.png', '$2a$10$F6FL9a/VL.F0b2Urq/tg3.4i7qbEYrvblrVoyaKWKRATob3bCtRpC', '0', '0', '2025-05-02 10:28:04', 1, '2025-05-02 10:29:52', 1918130336226271232, 0);

-- ----------------------------
-- Table structure for gen_table
-- ----------------------------
DROP TABLE IF EXISTS `gen_table`;
CREATE TABLE `gen_table`  (
  `table_id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `table_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '表名称',
  `table_comment` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '表描述',
  `sub_table_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '关联子表的表名',
  `sub_table_fk_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '子表关联的外键名',
  `class_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '实体类名称',
  `tpl_category` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'crud' COMMENT '使用的模板（crud单表操作 tree树表操作 sub主子表操作）',
  `package_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '生成包路径',
  `module_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '生成模块名',
  `business_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '生成业务名',
  `function_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '生成功能名',
  `function_author` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '生成功能作者',
  `form_col_num` int NULL DEFAULT 1 COMMENT '表单布局（单列 双列 三列）',
  `gen_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '生成代码方式（0zip压缩包 1自定义路径）',
  `gen_path` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '/' COMMENT '生成路径（不填默认项目路径）',
  `options` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '其它生成选项',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`table_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '代码生成业务表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of gen_table
-- ----------------------------

-- ----------------------------
-- Table structure for gen_table_column
-- ----------------------------
DROP TABLE IF EXISTS `gen_table_column`;
CREATE TABLE `gen_table_column`  (
  `column_id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `table_id` bigint NULL DEFAULT NULL COMMENT '归属表编号',
  `column_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '列名称',
  `column_comment` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '列描述',
  `column_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '列类型',
  `java_type` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'JAVA类型',
  `java_field` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'JAVA字段名',
  `is_pk` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '是否主键（1是）',
  `is_increment` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '是否自增（1是）',
  `is_required` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '是否必填（1是）',
  `is_insert` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '是否为插入字段（1是）',
  `is_edit` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '是否编辑字段（1是）',
  `is_list` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '是否列表字段（1是）',
  `is_query` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '是否查询字段（1是）',
  `query_type` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'EQ' COMMENT '查询方式（等于、不等于、大于、小于、范围）',
  `html_type` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '显示类型（文本框、文本域、下拉框、复选框、单选框、日期控件）',
  `dict_type` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '字典类型',
  `sort` int NULL DEFAULT NULL COMMENT '排序',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`column_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 149 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '代码生成业务表字段' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of gen_table_column
-- ----------------------------
INSERT INTO `gen_table_column` VALUES (68, 7, 'id', 'id', 'bigint', 'String', 'id', '1', '0', NULL, '1', NULL, '1', '1', 'EQ', 'input', '', 1, 'admin', '2025-04-28 05:30:11', NULL, '2025-04-28 10:21:05');
INSERT INTO `gen_table_column` VALUES (69, 7, 'title', '标题', 'varchar(256)', 'String', 'title', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'input', '', 2, 'admin', '2025-04-28 05:30:11', NULL, '2025-04-28 10:21:05');
INSERT INTO `gen_table_column` VALUES (70, 7, 'content', '正文内容', 'longtext', 'String', 'content', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'summernote', '', 3, 'admin', '2025-04-28 05:30:11', NULL, '2025-04-28 10:21:05');
INSERT INTO `gen_table_column` VALUES (71, 7, 'summary', '摘要', 'varchar(1024)', 'String', 'summary', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'textarea', '', 4, 'admin', '2025-04-28 05:30:11', NULL, '2025-04-28 10:21:05');
INSERT INTO `gen_table_column` VALUES (72, 7, 'category_id', '分类', 'bigint', 'Long', 'categoryId', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'input', '', 5, 'admin', '2025-04-28 05:30:11', NULL, '2025-04-28 10:21:05');
INSERT INTO `gen_table_column` VALUES (73, 7, 'thumbnail', '缩略图', 'varchar(1024)', 'String', 'thumbnail', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'textarea', '', 6, 'admin', '2025-04-28 05:30:11', NULL, '2025-04-28 10:21:05');
INSERT INTO `gen_table_column` VALUES (74, 7, 'view_count', '浏览量', 'bigint', 'Long', 'viewCount', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'input', '', 7, 'admin', '2025-04-28 05:30:11', NULL, '2025-04-28 10:21:05');
INSERT INTO `gen_table_column` VALUES (75, 7, 'comment_count', '评论量', 'bigint', 'Long', 'commentCount', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'input', '', 8, 'admin', '2025-04-28 05:30:11', NULL, '2025-04-28 10:21:05');
INSERT INTO `gen_table_column` VALUES (76, 7, 'create_by', '作者', 'bigint', 'String', 'createBy', '0', '0', '1', '1', NULL, '1', '1', 'EQ', 'input', '', 9, 'admin', '2025-04-28 05:30:11', NULL, '2025-04-28 10:21:05');
INSERT INTO `gen_table_column` VALUES (77, 7, 'create_time', '创建时间', 'timestamp', 'Date', 'createTime', '0', '0', '1', '1', NULL, '1', '1', 'EQ', 'datetime', '', 10, 'admin', '2025-04-28 05:30:11', NULL, '2025-04-28 10:21:05');
INSERT INTO `gen_table_column` VALUES (78, 7, 'update_by', '更新者', 'bigint', 'String', 'updateBy', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'input', '', 11, 'admin', '2025-04-28 05:30:11', NULL, '2025-04-28 10:21:05');
INSERT INTO `gen_table_column` VALUES (79, 7, 'update_time', '更新时间', 'timestamp', 'Date', 'updateTime', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'datetime', '', 12, 'admin', '2025-04-28 05:30:11', NULL, '2025-04-28 10:21:05');
INSERT INTO `gen_table_column` VALUES (80, 7, 'status', '状态：0=审核中 1=正常 2=下架', 'int', 'Long', 'status', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'radio', '', 13, 'admin', '2025-04-28 05:30:11', NULL, '2025-04-28 10:21:05');
INSERT INTO `gen_table_column` VALUES (81, 7, 'del_flag', '是否删除0=否', 'int', 'Long', 'delFlag', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'input', '', 14, 'admin', '2025-04-28 05:30:11', NULL, '2025-04-28 10:21:05');
INSERT INTO `gen_table_column` VALUES (82, 7, 'is_markdown', '是否是markdown', 'tinyint(1)', 'Integer', 'isMarkdown', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'input', '', 15, 'admin', '2025-04-28 05:30:11', NULL, '2025-04-28 10:21:05');
INSERT INTO `gen_table_column` VALUES (83, 7, 'image_urls', '配图地址，JSON格式', 'varchar(1024)', 'String', 'imageUrls', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'textarea', '', 16, 'admin', '2025-04-28 05:30:11', NULL, '2025-04-28 10:21:05');
INSERT INTO `gen_table_column` VALUES (84, 7, 'share_link', '分享链接', 'varchar(256)', 'String', 'shareLink', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 17, 'admin', '2025-04-28 05:30:11', NULL, '2025-04-28 10:21:05');
INSERT INTO `gen_table_column` VALUES (85, 7, 'need_password', '是否需要提取密码', 'tinyint(1)', 'Integer', 'needPassword', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'input', '', 18, 'admin', '2025-04-28 05:30:11', NULL, '2025-04-28 10:21:05');
INSERT INTO `gen_table_column` VALUES (86, 7, 'password', '提取密码', 'varchar(50)', 'String', 'password', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 19, 'admin', '2025-04-28 05:30:11', NULL, '2025-04-28 10:21:05');
INSERT INTO `gen_table_column` VALUES (87, 7, 'tags', '自定义标签，JSON格式', 'varchar(1024)', 'String', 'tags', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'textarea', '', 20, 'admin', '2025-04-28 05:30:11', NULL, '2025-04-28 10:21:05');
INSERT INTO `gen_table_column` VALUES (88, 7, 'top_tag', '顶级标签', 'varchar(50)', 'String', 'topTag', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 21, 'admin', '2025-04-28 05:30:11', NULL, '2025-04-28 10:21:05');
INSERT INTO `gen_table_column` VALUES (89, 8, 'user_id', '用户ID', 'bigint', 'String', 'userId', '1', '0', NULL, '1', NULL, '1', '1', 'EQ', 'input', '', 1, 'admin', '2025-04-28 05:30:11', NULL, '2025-04-28 10:18:26');
INSERT INTO `gen_table_column` VALUES (90, 8, 'total_capacity', '总容量（字节）', 'bigint', 'String', 'totalCapacity', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'input', '', 2, 'admin', '2025-04-28 05:30:11', NULL, '2025-04-28 10:18:26');
INSERT INTO `gen_table_column` VALUES (91, 8, 'used_capacity', '已用容量（字节）', 'bigint', 'String', 'usedCapacity', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'input', '', 3, 'admin', '2025-04-28 05:30:11', NULL, '2025-04-28 10:18:26');
INSERT INTO `gen_table_column` VALUES (92, 8, 'create_time', '创建时间', 'timestamp', 'Date', 'createTime', '0', '0', '1', '1', NULL, NULL, NULL, 'EQ', 'datetime', '', 4, 'admin', '2025-04-28 05:30:11', NULL, '2025-04-28 10:18:26');
INSERT INTO `gen_table_column` VALUES (93, 8, 'update_time', '更新时间', 'timestamp', 'Date', 'updateTime', '0', '0', '1', '1', '1', '1', NULL, 'EQ', 'datetime', '', 5, 'admin', '2025-04-28 05:30:11', NULL, '2025-04-28 10:18:26');
INSERT INTO `gen_table_column` VALUES (94, 9, 'id', 'id', 'bigint', 'String', 'id', '1', '0', NULL, '1', NULL, '1', '1', 'EQ', 'input', '', 1, 'admin', '2025-04-28 05:30:11', NULL, '2025-04-28 10:18:52');
INSERT INTO `gen_table_column` VALUES (95, 9, 'article_id', '所属文章', 'bigint', 'String', 'articleId', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'input', '', 2, 'admin', '2025-04-28 05:30:11', NULL, '2025-04-28 10:18:52');
INSERT INTO `gen_table_column` VALUES (96, 9, 'content', '评论内容', 'varchar(1000)', 'String', 'content', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'summernote', '', 3, 'admin', '2025-04-28 05:30:11', NULL, '2025-04-28 10:18:52');
INSERT INTO `gen_table_column` VALUES (97, 9, 'create_by', '创建者', 'bigint', 'String', 'createBy', '0', '0', '1', '1', NULL, '1', '1', 'EQ', 'input', '', 4, 'admin', '2025-04-28 05:30:11', NULL, '2025-04-28 10:18:52');
INSERT INTO `gen_table_column` VALUES (98, 9, 'create_time', '创建时间', 'timestamp', 'Date', 'createTime', '0', '0', '1', '1', NULL, NULL, NULL, 'EQ', 'datetime', '', 5, 'admin', '2025-04-28 05:30:11', NULL, '2025-04-28 10:18:52');
INSERT INTO `gen_table_column` VALUES (99, 9, 'nick_name', '用户昵称', 'varchar(64)', 'String', 'nickName', '0', '0', '1', '1', '1', '1', '1', 'LIKE', 'input', '', 6, 'admin', '2025-04-28 05:30:11', NULL, '2025-04-28 10:18:52');
INSERT INTO `gen_table_column` VALUES (100, 9, 'avatar', '用户头像', 'varchar(255)', 'String', 'avatar', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 7, 'admin', '2025-04-28 05:30:11', NULL, '2025-04-28 10:18:52');
INSERT INTO `gen_table_column` VALUES (101, 9, 'update_by', '修改人', 'bigint', 'String', 'updateBy', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'input', '', 8, 'admin', '2025-04-28 05:30:11', NULL, '2025-04-28 10:18:52');
INSERT INTO `gen_table_column` VALUES (102, 9, 'update_time', '修改时间', 'timestamp', 'Date', 'updateTime', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'datetime', '', 9, 'admin', '2025-04-28 05:30:11', NULL, '2025-04-28 10:18:52');
INSERT INTO `gen_table_column` VALUES (103, 9, 'del_flag', '是否删除0=否1=是', 'int', 'Long', 'delFlag', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'input', '', 10, 'admin', '2025-04-28 05:30:11', NULL, '2025-04-28 10:18:52');
INSERT INTO `gen_table_column` VALUES (104, 10, 'id', 'id', 'bigint', 'String', 'id', '1', '0', NULL, '1', NULL, '1', '1', 'EQ', 'input', '', 1, 'admin', '2025-04-28 05:30:11', NULL, '2025-04-28 10:20:08');
INSERT INTO `gen_table_column` VALUES (105, 10, 'user_id', '用户ID', 'bigint', 'String', 'userId', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'input', '', 2, 'admin', '2025-04-28 05:30:11', NULL, '2025-04-28 10:20:08');
INSERT INTO `gen_table_column` VALUES (106, 10, 'nick_name', '用户昵称', 'varchar(255)', 'String', 'nickName', '0', '0', '1', '1', '1', '1', '1', 'LIKE', 'input', '', 3, 'admin', '2025-04-28 05:30:11', NULL, '2025-04-28 10:20:08');
INSERT INTO `gen_table_column` VALUES (107, 10, 'file_name', '文件名称', 'varchar(256)', 'String', 'fileName', '0', '0', '1', '1', '1', '1', '1', 'LIKE', 'input', '', 4, 'admin', '2025-04-28 05:30:11', NULL, '2025-04-28 10:20:08');
INSERT INTO `gen_table_column` VALUES (108, 10, 'file_size', '文件大小（字节）', 'bigint', 'String', 'fileSize', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'input', '', 5, 'admin', '2025-04-28 05:30:11', NULL, '2025-04-28 10:20:08');
INSERT INTO `gen_table_column` VALUES (109, 10, 'need_password', '是否需要提取密码：0=不需要 1=需要', 'tinyint(1)', 'Integer', 'needPassword', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'input', '', 6, 'admin', '2025-04-28 05:30:11', NULL, '2025-04-28 10:20:08');
INSERT INTO `gen_table_column` VALUES (110, 10, 'password', '提取密码', 'varchar(50)', 'String', 'password', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 7, 'admin', '2025-04-28 05:30:11', NULL, '2025-04-28 10:20:08');
INSERT INTO `gen_table_column` VALUES (111, 10, 'minio_object_name', 'Minio对象名称', 'varchar(1024)', 'String', 'minioObjectName', '0', '0', NULL, '1', '1', '1', '1', 'LIKE', 'textarea', '', 8, 'admin', '2025-04-28 05:30:11', NULL, '2025-04-28 10:20:08');
INSERT INTO `gen_table_column` VALUES (112, 10, 'create_by', '创建者', 'bigint', 'String', 'createBy', '0', '0', '1', '1', NULL, '1', '1', 'EQ', 'input', '', 9, 'admin', '2025-04-28 05:30:11', NULL, '2025-04-28 10:20:08');
INSERT INTO `gen_table_column` VALUES (113, 10, 'create_time', '创建时间', 'timestamp', 'Date', 'createTime', '0', '0', '1', '1', NULL, '1', '1', 'EQ', 'datetime', '', 10, 'admin', '2025-04-28 05:30:11', NULL, '2025-04-28 10:20:08');
INSERT INTO `gen_table_column` VALUES (114, 10, 'update_by', '更新者', 'bigint', 'String', 'updateBy', '0', '0', '1', '1', '1', NULL, NULL, 'EQ', 'input', '', 11, 'admin', '2025-04-28 05:30:11', NULL, '2025-04-28 10:20:08');
INSERT INTO `gen_table_column` VALUES (115, 10, 'update_time', '更新时间', 'timestamp', 'Date', 'updateTime', '0', '0', '1', '1', '1', NULL, NULL, 'EQ', 'datetime', '', 12, 'admin', '2025-04-28 05:30:11', NULL, '2025-04-28 10:20:08');
INSERT INTO `gen_table_column` VALUES (116, 10, 'status', '状态：0=审核中 1=正常 2=下架', 'int', 'Long', 'status', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'radio', '', 13, 'admin', '2025-04-28 05:30:11', NULL, '2025-04-28 10:20:08');
INSERT INTO `gen_table_column` VALUES (117, 10, 'del_flag', '是否删除0=否', 'int', 'Long', 'delFlag', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'input', '', 14, 'admin', '2025-04-28 05:30:11', NULL, '2025-04-28 10:20:08');
INSERT INTO `gen_table_column` VALUES (118, 11, 'id', '主键ID', 'bigint', 'String', 'id', '1', '0', NULL, '1', NULL, '1', '1', 'EQ', 'input', '', 1, 'admin', '2025-04-28 05:30:11', NULL, '2025-04-28 10:20:29');
INSERT INTO `gen_table_column` VALUES (119, 11, 'user_id', '用户ID', 'bigint', 'String', 'userId', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'input', '', 2, 'admin', '2025-04-28 05:30:11', NULL, '2025-04-28 10:20:29');
INSERT INTO `gen_table_column` VALUES (120, 11, 'minio_object_name', 'MinIO对象名称', 'varchar(1024)', 'String', 'minioObjectName', '0', '0', NULL, '1', '1', '1', '1', 'LIKE', 'textarea', '', 3, 'admin', '2025-04-28 05:30:11', NULL, '2025-04-28 10:20:29');
INSERT INTO `gen_table_column` VALUES (121, 11, 'create_time', '创建时间', 'timestamp', 'Date', 'createTime', '0', '0', '1', '1', NULL, '1', '1', 'EQ', 'datetime', '', 4, 'admin', '2025-04-28 05:30:11', NULL, '2025-04-28 10:20:29');
INSERT INTO `gen_table_column` VALUES (122, 11, 'del_flag', '是否删除（0=未删除，1=已删除）', 'int', 'Long', 'delFlag', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'input', '', 5, 'admin', '2025-04-28 05:30:11', NULL, '2025-04-28 10:20:29');
INSERT INTO `gen_table_column` VALUES (123, 12, 'id', 'id', 'bigint', 'String', 'id', '1', '0', NULL, '1', NULL, '1', '1', 'EQ', 'input', '', 1, 'admin', '2025-04-28 05:30:11', NULL, '2025-04-28 10:20:58');
INSERT INTO `gen_table_column` VALUES (124, 12, 'email', '邮箱', 'varchar(64)', 'String', 'email', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'input', '', 2, 'admin', '2025-04-28 05:30:11', NULL, '2025-04-28 10:20:58');
INSERT INTO `gen_table_column` VALUES (125, 12, 'nickname', '昵称', 'varchar(64)', 'String', 'nickname', '0', '0', '1', '1', '1', '1', '1', 'LIKE', 'input', '', 3, 'admin', '2025-04-28 05:30:11', NULL, '2025-04-28 10:20:58');
INSERT INTO `gen_table_column` VALUES (126, 12, 'avatar', '头像', 'varchar(255)', 'String', 'avatar', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 4, 'admin', '2025-04-28 05:30:11', NULL, '2025-04-28 10:20:58');
INSERT INTO `gen_table_column` VALUES (127, 12, 'password', '密码', 'varchar(300)', 'String', 'password', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'input', '', 5, 'admin', '2025-04-28 05:30:11', NULL, '2025-04-28 10:20:58');
INSERT INTO `gen_table_column` VALUES (128, 12, 'role', '类型：0=普通用户1=管理员', 'char(1)', 'String', 'role', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'input', '', 6, 'admin', '2025-04-28 05:30:11', NULL, '2025-04-28 10:20:58');
INSERT INTO `gen_table_column` VALUES (129, 12, 'status', '状态：0=正常1=封禁', 'char(1)', 'String', 'status', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'radio', '', 7, 'admin', '2025-04-28 05:30:11', NULL, '2025-04-28 10:20:58');
INSERT INTO `gen_table_column` VALUES (130, 12, 'create_time', '创建时间', 'timestamp', 'Date', 'createTime', '0', '0', '1', '1', NULL, NULL, NULL, 'EQ', 'datetime', '', 8, 'admin', '2025-04-28 05:30:11', NULL, '2025-04-28 10:20:58');
INSERT INTO `gen_table_column` VALUES (131, 12, 'create_by', '创建人', 'bigint', 'String', 'createBy', '0', '0', '1', '1', NULL, NULL, NULL, 'EQ', 'input', '', 9, 'admin', '2025-04-28 05:30:11', NULL, '2025-04-28 10:20:58');
INSERT INTO `gen_table_column` VALUES (132, 12, 'update_time', '更新时间', 'timestamp', 'Date', 'updateTime', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'datetime', '', 10, 'admin', '2025-04-28 05:30:11', NULL, '2025-04-28 10:20:58');
INSERT INTO `gen_table_column` VALUES (133, 12, 'update_by', '更新人', 'bigint', 'String', 'updateBy', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'input', '', 11, 'admin', '2025-04-28 05:30:11', NULL, '2025-04-28 10:20:58');
INSERT INTO `gen_table_column` VALUES (134, 12, 'del_flag', '0=未删除1=已删除', 'int', 'Long', 'delFlag', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'input', '', 12, 'admin', '2025-04-28 05:30:11', NULL, '2025-04-28 10:20:58');
INSERT INTO `gen_table_column` VALUES (135, 13, 'id', '举报记录ID', 'bigint', 'String', 'id', '1', '0', NULL, '1', NULL, '1', '1', 'EQ', 'input', '', 1, 'admin', '2025-04-28 10:01:19', NULL, '2025-04-28 10:18:07');
INSERT INTO `gen_table_column` VALUES (136, 13, 'blog_id', '被举报文章ID', 'bigint', 'String', 'blogId', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'input', '', 2, 'admin', '2025-04-28 10:01:19', NULL, '2025-04-28 10:18:07');
INSERT INTO `gen_table_column` VALUES (137, 13, 'report_user', '举报人ID', 'bigint', 'String', 'reportUser', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'input', '', 3, 'admin', '2025-04-28 10:01:19', NULL, '2025-04-28 10:18:07');
INSERT INTO `gen_table_column` VALUES (138, 13, 'report_type', '举报类型：0=垃圾广告 1=违规内容 2=侵权 3=其他', 'int', 'Long', 'reportType', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'select', '', 4, 'admin', '2025-04-28 10:01:19', NULL, '2025-04-28 10:18:07');
INSERT INTO `gen_table_column` VALUES (139, 13, 'report_content', '举报详情', 'varchar(1024)', 'String', 'reportContent', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'summernote', '', 5, 'admin', '2025-04-28 10:01:19', NULL, '2025-04-28 10:18:07');
INSERT INTO `gen_table_column` VALUES (140, 13, 'evidence', '证据材料(JSON格式)', 'varchar(1024)', 'String', 'evidence', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'textarea', '', 6, 'admin', '2025-04-28 10:01:19', NULL, '2025-04-28 10:18:07');
INSERT INTO `gen_table_column` VALUES (141, 13, 'report_time', '举报时间', 'timestamp', 'Date', 'reportTime', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'datetime', '', 7, 'admin', '2025-04-28 10:01:19', NULL, '2025-04-28 10:18:07');
INSERT INTO `gen_table_column` VALUES (142, 13, 'handler', '处理人ID', 'bigint', 'String', 'handler', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 8, 'admin', '2025-04-28 10:01:19', NULL, '2025-04-28 10:18:07');
INSERT INTO `gen_table_column` VALUES (143, 13, 'handle_time', '处理时间', 'timestamp', 'Date', 'handleTime', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'datetime', '', 9, 'admin', '2025-04-28 10:01:19', NULL, '2025-04-28 10:18:07');
INSERT INTO `gen_table_column` VALUES (144, 13, 'status', '状态：0=待处理 1=已处理 2=已驳回', 'int', 'Long', 'status', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'radio', '', 10, 'admin', '2025-04-28 10:01:19', NULL, '2025-04-28 10:18:07');
INSERT INTO `gen_table_column` VALUES (145, 13, 'handle_remark', '处理备注', 'varchar(512)', 'String', 'handleRemark', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'textarea', '', 11, 'admin', '2025-04-28 10:01:19', NULL, '2025-04-28 10:18:07');
INSERT INTO `gen_table_column` VALUES (146, 13, 'del_flag', '删除标志：0=有效 1=删除', 'int', 'Long', 'delFlag', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'input', '', 12, 'admin', '2025-04-28 10:01:19', NULL, '2025-04-28 10:18:07');
INSERT INTO `gen_table_column` VALUES (147, 13, 'create_time', '创建时间', 'timestamp', 'Date', 'createTime', '0', '0', '1', '1', NULL, NULL, NULL, 'EQ', 'datetime', '', 13, 'admin', '2025-04-28 10:01:19', NULL, '2025-04-28 10:18:07');
INSERT INTO `gen_table_column` VALUES (148, 13, 'update_time', '更新时间', 'timestamp', 'Date', 'updateTime', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'datetime', '', 14, 'admin', '2025-04-28 10:01:19', NULL, '2025-04-28 10:18:07');

SET FOREIGN_KEY_CHECKS = 1;
