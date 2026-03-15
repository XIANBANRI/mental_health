-- MySQL dump 10.13  Distrib 8.0.40, for Win64 (x86_64)
--
-- Host: localhost    Database: mental_health
-- ------------------------------------------------------
-- Server version	8.0.40

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `admin`
--

DROP TABLE IF EXISTS `admin`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `admin` (
  `account` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '账号',
  `password` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '密码',
  PRIMARY KEY (`account`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='管理员表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `admin`
--

LOCK TABLES `admin` WRITE;
/*!40000 ALTER TABLE `admin` DISABLE KEYS */;
INSERT INTO `admin` VALUES ('admin','admin@123456'),('operator','operator@654321');
/*!40000 ALTER TABLE `admin` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `appointment`
--

DROP TABLE IF EXISTS `appointment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `appointment` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `appointment_no` varchar(50) COLLATE utf8mb4_general_ci NOT NULL COMMENT '预约编号',
  `student_account` varchar(50) COLLATE utf8mb4_general_ci NOT NULL COMMENT '学生账号或学号',
  `teacher_account` varchar(50) COLLATE utf8mb4_general_ci NOT NULL COMMENT '心理老师账号',
  `schedule_id` bigint NOT NULL COMMENT '办公时间ID',
  `appointment_date` date NOT NULL COMMENT '预约日期',
  `start_time` time NOT NULL COMMENT '预约开始时间',
  `end_time` time NOT NULL COMMENT '预约结束时间',
  `purpose` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '预约目的',
  `remark` varchar(500) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '学生备注',
  `teacher_reply` varchar(500) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '老师回复',
  `status` varchar(20) COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'PENDING' COMMENT 'PENDING待审核 APPROVED已通过 REJECTED已拒绝 CANCELLED已取消 COMPLETED已完成',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `approved_at` datetime DEFAULT NULL,
  `cancelled_at` datetime DEFAULT NULL,
  `completed_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_appointment_no` (`appointment_no`),
  KEY `fk_appointment_schedule` (`schedule_id`),
  KEY `idx_appointment_student_account` (`student_account`),
  KEY `idx_appointment_teacher_account` (`teacher_account`),
  KEY `idx_appointment_date` (`appointment_date`),
  KEY `idx_appointment_status` (`status`),
  CONSTRAINT `fk_appointment_schedule` FOREIGN KEY (`schedule_id`) REFERENCES `teacher_schedule` (`id`),
  CONSTRAINT `fk_appointment_teacher` FOREIGN KEY (`teacher_account`) REFERENCES `teacher` (`account`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='学生心理预约表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `appointment`
--

LOCK TABLES `appointment` WRITE;
/*!40000 ALTER TABLE `appointment` DISABLE KEYS */;
/*!40000 ALTER TABLE `appointment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `assessment_answer`
--

DROP TABLE IF EXISTS `assessment_answer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `assessment_answer` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `record_id` bigint NOT NULL,
  `question_id` bigint NOT NULL,
  `option_id` bigint NOT NULL,
  `answer_score` int NOT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `fk_answer_record` (`record_id`),
  KEY `fk_answer_question` (`question_id`),
  KEY `fk_answer_option` (`option_id`),
  CONSTRAINT `fk_answer_option` FOREIGN KEY (`option_id`) REFERENCES `assessment_option` (`id`),
  CONSTRAINT `fk_answer_question` FOREIGN KEY (`question_id`) REFERENCES `assessment_question` (`id`),
  CONSTRAINT `fk_answer_record` FOREIGN KEY (`record_id`) REFERENCES `assessment_record` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='心理测评作答明细表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `assessment_answer`
--

LOCK TABLES `assessment_answer` WRITE;
/*!40000 ALTER TABLE `assessment_answer` DISABLE KEYS */;
INSERT INTO `assessment_answer` VALUES (1,1,85,530,1,'2026-03-15 11:01:32'),(2,1,86,529,1,'2026-03-15 11:01:32'),(3,1,87,528,1,'2026-03-15 11:01:32'),(4,1,88,527,1,'2026-03-15 11:01:32'),(5,1,89,526,1,'2026-03-15 11:01:32'),(6,1,90,525,1,'2026-03-15 11:01:32'),(7,1,91,524,1,'2026-03-15 11:01:32'),(8,1,92,523,1,'2026-03-15 11:01:32'),(9,1,93,522,1,'2026-03-15 11:01:32'),(10,1,94,521,1,'2026-03-15 11:01:32'),(11,1,69,463,2,'2026-03-15 11:01:45'),(12,1,70,464,2,'2026-03-15 11:01:45'),(13,1,71,465,2,'2026-03-15 11:01:45'),(14,1,72,466,2,'2026-03-15 11:01:45'),(15,1,73,467,2,'2026-03-15 11:01:45'),(16,1,74,468,2,'2026-03-15 11:01:45'),(17,1,75,469,2,'2026-03-15 11:01:45'),(18,1,76,470,2,'2026-03-15 11:01:45'),(19,1,77,471,2,'2026-03-15 11:01:45'),(20,1,78,507,2,'2026-03-15 11:01:55'),(21,1,79,508,2,'2026-03-15 11:01:55'),(22,1,80,509,2,'2026-03-15 11:01:55'),(23,1,81,510,2,'2026-03-15 11:01:55'),(24,1,82,511,2,'2026-03-15 11:01:55'),(25,1,83,512,2,'2026-03-15 11:01:55'),(26,1,84,513,2,'2026-03-15 11:01:55');
/*!40000 ALTER TABLE `assessment_answer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `assessment_option`
--

DROP TABLE IF EXISTS `assessment_option`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `assessment_option` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `question_id` bigint NOT NULL,
  `option_no` int NOT NULL,
  `option_text` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `option_score` int NOT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `fk_option_question` (`question_id`),
  CONSTRAINT `fk_option_question` FOREIGN KEY (`question_id`) REFERENCES `assessment_question` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=571 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='心理测评选项表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `assessment_option`
--

LOCK TABLES `assessment_option` WRITE;
/*!40000 ALTER TABLE `assessment_option` DISABLE KEYS */;
INSERT INTO `assessment_option` VALUES (391,64,1,'从未',0,'2026-03-15 02:29:57'),(392,65,1,'从未',0,'2026-03-15 02:29:57'),(393,66,1,'从未',0,'2026-03-15 02:29:57'),(394,67,1,'从未',0,'2026-03-15 02:29:57'),(395,68,1,'从未',0,'2026-03-15 02:29:57'),(398,64,2,'偶尔',1,'2026-03-15 02:29:57'),(399,65,2,'偶尔',1,'2026-03-15 02:29:57'),(400,66,2,'偶尔',1,'2026-03-15 02:29:57'),(401,67,2,'偶尔',1,'2026-03-15 02:29:57'),(402,68,2,'偶尔',1,'2026-03-15 02:29:57'),(405,64,3,'少于一半时间',2,'2026-03-15 02:29:57'),(406,65,3,'少于一半时间',2,'2026-03-15 02:29:57'),(407,66,3,'少于一半时间',2,'2026-03-15 02:29:57'),(408,67,3,'少于一半时间',2,'2026-03-15 02:29:57'),(409,68,3,'少于一半时间',2,'2026-03-15 02:29:57'),(412,64,4,'超过一半时间',3,'2026-03-15 02:29:57'),(413,65,4,'超过一半时间',3,'2026-03-15 02:29:57'),(414,66,4,'超过一半时间',3,'2026-03-15 02:29:57'),(415,67,4,'超过一半时间',3,'2026-03-15 02:29:57'),(416,68,4,'超过一半时间',3,'2026-03-15 02:29:57'),(419,64,5,'大部分时间',4,'2026-03-15 02:29:57'),(420,65,5,'大部分时间',4,'2026-03-15 02:29:57'),(421,66,5,'大部分时间',4,'2026-03-15 02:29:57'),(422,67,5,'大部分时间',4,'2026-03-15 02:29:57'),(423,68,5,'大部分时间',4,'2026-03-15 02:29:57'),(426,64,6,'一直如此',5,'2026-03-15 02:29:57'),(427,65,6,'一直如此',5,'2026-03-15 02:29:57'),(428,66,6,'一直如此',5,'2026-03-15 02:29:57'),(429,67,6,'一直如此',5,'2026-03-15 02:29:57'),(430,68,6,'一直如此',5,'2026-03-15 02:29:57'),(433,69,1,'完全没有',0,'2026-03-15 02:29:57'),(434,70,1,'完全没有',0,'2026-03-15 02:29:57'),(435,71,1,'完全没有',0,'2026-03-15 02:29:57'),(436,72,1,'完全没有',0,'2026-03-15 02:29:57'),(437,73,1,'完全没有',0,'2026-03-15 02:29:57'),(438,74,1,'完全没有',0,'2026-03-15 02:29:57'),(439,75,1,'完全没有',0,'2026-03-15 02:29:57'),(440,76,1,'完全没有',0,'2026-03-15 02:29:57'),(441,77,1,'完全没有',0,'2026-03-15 02:29:57'),(448,69,2,'几天',1,'2026-03-15 02:29:57'),(449,70,2,'几天',1,'2026-03-15 02:29:57'),(450,71,2,'几天',1,'2026-03-15 02:29:57'),(451,72,2,'几天',1,'2026-03-15 02:29:57'),(452,73,2,'几天',1,'2026-03-15 02:29:57'),(453,74,2,'几天',1,'2026-03-15 02:29:57'),(454,75,2,'几天',1,'2026-03-15 02:29:57'),(455,76,2,'几天',1,'2026-03-15 02:29:57'),(456,77,2,'几天',1,'2026-03-15 02:29:57'),(463,69,3,'一半以上天数',2,'2026-03-15 02:29:57'),(464,70,3,'一半以上天数',2,'2026-03-15 02:29:57'),(465,71,3,'一半以上天数',2,'2026-03-15 02:29:57'),(466,72,3,'一半以上天数',2,'2026-03-15 02:29:57'),(467,73,3,'一半以上天数',2,'2026-03-15 02:29:57'),(468,74,3,'一半以上天数',2,'2026-03-15 02:29:57'),(469,75,3,'一半以上天数',2,'2026-03-15 02:29:57'),(470,76,3,'一半以上天数',2,'2026-03-15 02:29:57'),(471,77,3,'一半以上天数',2,'2026-03-15 02:29:57'),(478,69,4,'几乎每天',3,'2026-03-15 02:29:57'),(479,70,4,'几乎每天',3,'2026-03-15 02:29:57'),(480,71,4,'几乎每天',3,'2026-03-15 02:29:57'),(481,72,4,'几乎每天',3,'2026-03-15 02:29:57'),(482,73,4,'几乎每天',3,'2026-03-15 02:29:57'),(483,74,4,'几乎每天',3,'2026-03-15 02:29:57'),(484,75,4,'几乎每天',3,'2026-03-15 02:29:57'),(485,76,4,'几乎每天',3,'2026-03-15 02:29:57'),(486,77,4,'几乎每天',3,'2026-03-15 02:29:57'),(493,78,1,'完全没有',0,'2026-03-15 02:29:57'),(494,79,1,'完全没有',0,'2026-03-15 02:29:57'),(495,80,1,'完全没有',0,'2026-03-15 02:29:57'),(496,81,1,'完全没有',0,'2026-03-15 02:29:57'),(497,82,1,'完全没有',0,'2026-03-15 02:29:57'),(498,83,1,'完全没有',0,'2026-03-15 02:29:57'),(499,84,1,'完全没有',0,'2026-03-15 02:29:57'),(500,78,2,'几天',1,'2026-03-15 02:29:57'),(501,79,2,'几天',1,'2026-03-15 02:29:57'),(502,80,2,'几天',1,'2026-03-15 02:29:57'),(503,81,2,'几天',1,'2026-03-15 02:29:57'),(504,82,2,'几天',1,'2026-03-15 02:29:57'),(505,83,2,'几天',1,'2026-03-15 02:29:57'),(506,84,2,'几天',1,'2026-03-15 02:29:57'),(507,78,3,'一半以上天数',2,'2026-03-15 02:29:57'),(508,79,3,'一半以上天数',2,'2026-03-15 02:29:57'),(509,80,3,'一半以上天数',2,'2026-03-15 02:29:57'),(510,81,3,'一半以上天数',2,'2026-03-15 02:29:57'),(511,82,3,'一半以上天数',2,'2026-03-15 02:29:57'),(512,83,3,'一半以上天数',2,'2026-03-15 02:29:57'),(513,84,3,'一半以上天数',2,'2026-03-15 02:29:57'),(514,78,4,'几乎每天',3,'2026-03-15 02:29:57'),(515,79,4,'几乎每天',3,'2026-03-15 02:29:57'),(516,80,4,'几乎每天',3,'2026-03-15 02:29:57'),(517,81,4,'几乎每天',3,'2026-03-15 02:29:57'),(518,82,4,'几乎每天',3,'2026-03-15 02:29:57'),(519,83,4,'几乎每天',3,'2026-03-15 02:29:57'),(520,84,4,'几乎每天',3,'2026-03-15 02:29:57'),(521,94,1,'从不',1,'2026-03-15 02:29:57'),(522,93,1,'从不',1,'2026-03-15 02:29:57'),(523,92,1,'从不',1,'2026-03-15 02:29:57'),(524,91,1,'从不',1,'2026-03-15 02:29:57'),(525,90,1,'从不',1,'2026-03-15 02:29:57'),(526,89,1,'从不',1,'2026-03-15 02:29:57'),(527,88,1,'从不',1,'2026-03-15 02:29:57'),(528,87,1,'从不',1,'2026-03-15 02:29:57'),(529,86,1,'从不',1,'2026-03-15 02:29:57'),(530,85,1,'从不',1,'2026-03-15 02:29:57'),(531,94,2,'偶尔',2,'2026-03-15 02:29:57'),(532,93,2,'偶尔',2,'2026-03-15 02:29:57'),(533,92,2,'偶尔',2,'2026-03-15 02:29:57'),(534,91,2,'偶尔',2,'2026-03-15 02:29:57'),(535,90,2,'偶尔',2,'2026-03-15 02:29:57'),(536,89,2,'偶尔',2,'2026-03-15 02:29:57'),(537,88,2,'偶尔',2,'2026-03-15 02:29:57'),(538,87,2,'偶尔',2,'2026-03-15 02:29:57'),(539,86,2,'偶尔',2,'2026-03-15 02:29:57'),(540,85,2,'偶尔',2,'2026-03-15 02:29:57'),(541,94,3,'有时',3,'2026-03-15 02:29:57'),(542,93,3,'有时',3,'2026-03-15 02:29:57'),(543,92,3,'有时',3,'2026-03-15 02:29:57'),(544,91,3,'有时',3,'2026-03-15 02:29:57'),(545,90,3,'有时',3,'2026-03-15 02:29:57'),(546,89,3,'有时',3,'2026-03-15 02:29:57'),(547,88,3,'有时',3,'2026-03-15 02:29:57'),(548,87,3,'有时',3,'2026-03-15 02:29:57'),(549,86,3,'有时',3,'2026-03-15 02:29:57'),(550,85,3,'有时',3,'2026-03-15 02:29:57'),(551,94,4,'经常',4,'2026-03-15 02:29:57'),(552,93,4,'经常',4,'2026-03-15 02:29:57'),(553,92,4,'经常',4,'2026-03-15 02:29:57'),(554,91,4,'经常',4,'2026-03-15 02:29:57'),(555,90,4,'经常',4,'2026-03-15 02:29:57'),(556,89,4,'经常',4,'2026-03-15 02:29:57'),(557,88,4,'经常',4,'2026-03-15 02:29:57'),(558,87,4,'经常',4,'2026-03-15 02:29:57'),(559,86,4,'经常',4,'2026-03-15 02:29:57'),(560,85,4,'经常',4,'2026-03-15 02:29:57'),(561,94,5,'总是',5,'2026-03-15 02:29:57'),(562,93,5,'总是',5,'2026-03-15 02:29:57'),(563,92,5,'总是',5,'2026-03-15 02:29:57'),(564,91,5,'总是',5,'2026-03-15 02:29:57'),(565,90,5,'总是',5,'2026-03-15 02:29:57'),(566,89,5,'总是',5,'2026-03-15 02:29:57'),(567,88,5,'总是',5,'2026-03-15 02:29:57'),(568,87,5,'总是',5,'2026-03-15 02:29:57'),(569,86,5,'总是',5,'2026-03-15 02:29:57'),(570,85,5,'总是',5,'2026-03-15 02:29:57');
/*!40000 ALTER TABLE `assessment_option` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `assessment_question`
--

DROP TABLE IF EXISTS `assessment_question`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `assessment_question` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `scale_id` bigint NOT NULL,
  `question_no` int NOT NULL,
  `question_text` varchar(500) COLLATE utf8mb4_unicode_ci NOT NULL,
  `required_flag` tinyint NOT NULL DEFAULT '1',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `fk_question_scale` (`scale_id`),
  CONSTRAINT `fk_question_scale` FOREIGN KEY (`scale_id`) REFERENCES `assessment_scale` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=95 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='心理测评题目表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `assessment_question`
--

LOCK TABLES `assessment_question` WRITE;
/*!40000 ALTER TABLE `assessment_question` DISABLE KEYS */;
INSERT INTO `assessment_question` VALUES (64,14,1,'过去两周，我感觉心情愉快、精神较好',1,'2026-03-15 02:29:57'),(65,14,2,'过去两周，我感觉平静、放松',1,'2026-03-15 02:29:57'),(66,14,3,'过去两周，我感觉精力充沛、做事有劲头',1,'2026-03-15 02:29:57'),(67,14,4,'过去两周，我起床后感觉休息充分',1,'2026-03-15 02:29:57'),(68,14,5,'过去两周，我的日常生活里有让我感兴趣的事情',1,'2026-03-15 02:29:57'),(69,15,1,'过去两周，做事兴趣下降或提不起劲',1,'2026-03-15 02:29:57'),(70,15,2,'过去两周，情绪低落、沮丧或感到无望',1,'2026-03-15 02:29:57'),(71,15,3,'过去两周，入睡困难、易醒或睡得过多',1,'2026-03-15 02:29:57'),(72,15,4,'过去两周，感到疲惫或缺乏精力',1,'2026-03-15 02:29:57'),(73,15,5,'过去两周，食欲差或吃得过多',1,'2026-03-15 02:29:57'),(74,15,6,'过去两周，对自己评价很差，觉得自己失败或让家人失望',1,'2026-03-15 02:29:57'),(75,15,7,'过去两周，难以集中注意力，如学习或阅读时难专心',1,'2026-03-15 02:29:57'),(76,15,8,'过去两周，动作或说话明显变慢，或坐立不安比平时更明显',1,'2026-03-15 02:29:57'),(77,15,9,'过去两周，出现过伤害自己或认为不如消失的想法',1,'2026-03-15 02:29:57'),(78,16,1,'过去两周，感到紧张、焦虑或神经绷得很紧',1,'2026-03-15 02:29:57'),(79,16,2,'过去两周，无法停止担忧或控制担忧',1,'2026-03-15 02:29:57'),(80,16,3,'过去两周，过度担心各种事情',1,'2026-03-15 02:29:57'),(81,16,4,'过去两周，难以放松下来',1,'2026-03-15 02:29:57'),(82,16,5,'过去两周，坐立不安，难以安静坐着',1,'2026-03-15 02:29:57'),(83,16,6,'过去两周，容易烦躁或被激怒',1,'2026-03-15 02:29:57'),(84,16,7,'过去两周，总担心会有糟糕的事情发生',1,'2026-03-15 02:29:57'),(85,4,1,'过去30天里，你是否无明显原因地感到疲惫？',1,'2026-03-15 02:29:57'),(86,4,2,'过去30天里，你是否感到紧张？',1,'2026-03-15 02:29:57'),(87,4,3,'过去30天里，你是否紧张到难以平静下来？',1,'2026-03-15 02:29:57'),(88,4,4,'过去30天里，你是否感到没有希望？',1,'2026-03-15 02:29:57'),(89,4,5,'过去30天里，你是否感到坐立不安或心烦意乱？',1,'2026-03-15 02:29:57'),(90,4,6,'过去30天里，你是否坐立不安到根本坐不住？',1,'2026-03-15 02:29:57'),(91,4,7,'过去30天里，你是否感到情绪低落？',1,'2026-03-15 02:29:57'),(92,4,8,'过去30天里，你是否觉得做任何事都很费力？',1,'2026-03-15 02:29:57'),(93,4,9,'过去30天里，你是否悲伤到没有什么能让你开心起来？',1,'2026-03-15 02:29:57'),(94,4,10,'过去30天里，你是否感到自己毫无价值？',1,'2026-03-15 02:29:57');
/*!40000 ALTER TABLE `assessment_question` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `assessment_record`
--

DROP TABLE IF EXISTS `assessment_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `assessment_record` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `student_id` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '学生学号',
  `semester` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '第1学期' COMMENT '学期',
  `k10_score` int DEFAULT NULL COMMENT 'K10原始分',
  `k10_status` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '未完成' COMMENT '已完成/未完成',
  `k10_level` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'K10等级',
  `k10_summary` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'K10结果说明',
  `who5_score` int DEFAULT NULL COMMENT 'WHO5原始分',
  `who5_status` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '未完成' COMMENT '已完成/未完成',
  `who5_level` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'WHO5等级',
  `who5_summary` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'WHO5结果说明',
  `phq9_score` int DEFAULT NULL COMMENT 'PHQ9原始分',
  `phq9_status` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '未完成' COMMENT '已完成/未完成',
  `phq9_level` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'PHQ9等级',
  `phq9_summary` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'PHQ9结果说明',
  `gad7_score` int DEFAULT NULL COMMENT 'GAD7原始分',
  `gad7_status` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '未完成' COMMENT '已完成/未完成',
  `gad7_level` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'GAD7等级',
  `gad7_summary` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'GAD7结果说明',
  `health_total_score` int DEFAULT NULL COMMENT '综合总分(0~100)',
  `health_status` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '未完成' COMMENT '健康/预警/风险较高/高风险/未完成',
  `health_summary` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT '四项量表未全部完成，暂不生成综合总分' COMMENT '综合结果说明',
  `submitted_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '最近提交时间',
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_student_semester` (`student_id`,`semester`),
  KEY `idx_record_student_id` (`student_id`),
  KEY `idx_record_semester` (`semester`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='心理测评汇总记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `assessment_record`
--

LOCK TABLES `assessment_record` WRITE;
/*!40000 ALTER TABLE `assessment_record` DISABLE KEYS */;
INSERT INTO `assessment_record` VALUES (1,'2023010101','第1学期',10,'已完成','低','心理困扰水平较低',4,'已完成','较差','当前幸福感偏低，建议进一步评估情绪状态',18,'已完成','中重度','抑郁风险较高',14,'已完成','中度','存在中度焦虑风险',54,'风险较高','综合风险较高，建议及时干预','2026-03-15 11:01:55','2026-03-15 11:01:55');
/*!40000 ALTER TABLE `assessment_record` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `assessment_result_rule`
--

DROP TABLE IF EXISTS `assessment_result_rule`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `assessment_result_rule` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `scale_id` bigint NOT NULL,
  `min_score` int NOT NULL,
  `max_score` int NOT NULL,
  `result_level` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `result_summary` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `suggestion` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `fk_rule_scale` (`scale_id`),
  CONSTRAINT `fk_rule_scale` FOREIGN KEY (`scale_id`) REFERENCES `assessment_scale` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=61 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='单量表结果判定规则表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `assessment_result_rule`
--

LOCK TABLES `assessment_result_rule` WRITE;
/*!40000 ALTER TABLE `assessment_result_rule` DISABLE KEYS */;
INSERT INTO `assessment_result_rule` VALUES (13,4,10,15,'低','心理困扰水平较低','建议保持当前状态','2026-03-15 02:29:57'),(14,4,16,21,'中等','存在一定心理困扰','建议关注近期压力事件，必要时复测','2026-03-15 02:29:57'),(15,4,22,29,'高','心理困扰水平较高','建议预约心理咨询','2026-03-15 02:29:57'),(16,4,30,50,'很高','心理困扰水平很高','建议尽快寻求专业帮助','2026-03-15 02:29:57'),(50,14,0,12,'较差','当前幸福感偏低，建议进一步评估情绪状态','建议继续做 PHQ-9 或联系心理老师','2026-03-15 02:29:57'),(51,14,13,25,'正常','当前幸福感总体正常','建议保持规律作息并定期复测','2026-03-15 02:29:57'),(52,15,0,4,'无或极轻','当前抑郁症状较少','建议保持规律生活并持续关注情绪变化','2026-03-15 02:29:57'),(53,15,5,9,'轻度','存在轻度抑郁倾向','建议一到两周后复测，并关注睡眠与压力','2026-03-15 02:29:57'),(54,15,10,14,'中度','存在中度抑郁风险','建议预约心理咨询或联系辅导员','2026-03-15 02:29:57'),(55,15,15,19,'中重度','抑郁风险较高','建议尽快进行专业心理评估','2026-03-15 02:29:57'),(56,15,20,27,'重度','抑郁风险高','建议尽快联系专业心理老师或医疗机构','2026-03-15 02:29:57'),(57,16,0,4,'无或极轻','当前焦虑症状较少','建议保持稳定作息和运动','2026-03-15 02:29:57'),(58,16,5,9,'轻度','存在轻度焦虑倾向','建议关注压力来源并进行自我调节','2026-03-15 02:29:57'),(59,16,10,14,'中度','存在中度焦虑风险','建议进一步咨询心理老师','2026-03-15 02:29:57'),(60,16,15,21,'重度','焦虑风险较高','建议尽快接受专业评估','2026-03-15 02:29:57');
/*!40000 ALTER TABLE `assessment_result_rule` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `assessment_scale`
--

DROP TABLE IF EXISTS `assessment_scale`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `assessment_scale` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `scale_code` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `scale_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `scale_type` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `description` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `question_count` int NOT NULL,
  `score_min` int NOT NULL,
  `score_max` int NOT NULL,
  `status` tinyint NOT NULL DEFAULT '1',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_scale_code` (`scale_code`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='心理测评量表表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `assessment_scale`
--

LOCK TABLES `assessment_scale` WRITE;
/*!40000 ALTER TABLE `assessment_scale` DISABLE KEYS */;
INSERT INTO `assessment_scale` VALUES (4,'K10','K10 心理困扰量表','distress','10题总体心理困扰筛查',10,10,50,1,'2026-03-15 02:29:57','2026-03-15 02:29:57'),(14,'WHO5','WHO-5 幸福感指数','wellbeing','5题快速幸福感/心理状态筛查',5,0,25,1,'2026-03-15 02:29:57','2026-03-15 02:29:57'),(15,'PHQ9','PHQ-9 抑郁筛查','depression','9题抑郁症状筛查',9,0,27,1,'2026-03-15 02:29:57','2026-03-15 02:29:57'),(16,'GAD7','GAD-7 焦虑筛查','anxiety','7题焦虑症状筛查',7,0,21,1,'2026-03-15 02:29:57','2026-03-15 02:29:57');
/*!40000 ALTER TABLE `assessment_scale` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `assessment_total_rule`
--

DROP TABLE IF EXISTS `assessment_total_rule`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `assessment_total_rule` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `min_score` int NOT NULL,
  `max_score` int NOT NULL,
  `health_status` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '健康/预警/风险较高/高风险',
  `health_summary` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `suggestion` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='综合总分判定规则表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `assessment_total_rule`
--

LOCK TABLES `assessment_total_rule` WRITE;
/*!40000 ALTER TABLE `assessment_total_rule` DISABLE KEYS */;
INSERT INTO `assessment_total_rule` VALUES (1,0,24,'健康','整体心理状态健康，风险较低','建议保持规律作息、运动与良好社交','2026-03-15 02:29:57'),(2,25,49,'预警','存在一定心理波动，建议持续关注','建议近期复测，并关注学习、人际与睡眠情况','2026-03-15 02:29:57'),(3,50,74,'风险较高','综合风险较高，建议及时干预','建议预约心理咨询或联系辅导员','2026-03-15 02:29:57'),(4,75,100,'高风险','综合风险很高，建议尽快寻求专业帮助','建议尽快联系心理老师或专业医疗机构','2026-03-15 02:29:57');
/*!40000 ALTER TABLE `assessment_total_rule` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `counselor`
--

DROP TABLE IF EXISTS `counselor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `counselor` (
  `account` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '账号',
  `password` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '密码',
  `college` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '学院',
  `phone` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '手机号',
  PRIMARY KEY (`account`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='辅导员表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `counselor`
--

LOCK TABLES `counselor` WRITE;
/*!40000 ALTER TABLE `counselor` DISABLE KEYS */;
INSERT INTO `counselor` VALUES ('counselor01','counselor@01','计算机科学与技术学院','13700137001'),('counselor02','counselor@02','文学院','13700137002'),('counselor03','counselor@03','医学院','13700137003'),('counselor04','counselor@04','商学院','13700137004'),('counselor05','counselor@05','外国语学院','13700137005');
/*!40000 ALTER TABLE `counselor` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `student`
--

DROP TABLE IF EXISTS `student`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `student` (
  `student_id` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '学号',
  `name` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '姓名',
  `college` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '学院',
  `class_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '班级',
  `password` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '密码',
  `phone` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '手机号',
  `score` decimal(5,2) DEFAULT '0.00' COMMENT '分数',
  PRIMARY KEY (`student_id`),
  UNIQUE KEY `uk_student_phone` (`phone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='学生表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `student`
--

LOCK TABLES `student` WRITE;
/*!40000 ALTER TABLE `student` DISABLE KEYS */;
INSERT INTO `student` VALUES ('2023010101','张三','计算机科学与技术学院','软件工程2301班','123456','13800138001',85.50),('2023010102','李四','计算机科学与技术学院','软件工程2301班','123456aA!','13800138002',78.00),('2023020201','王五','文学院','汉语言文学2302班','123456aA!','13800138003',92.75),('2023020202','赵六','文学院','汉语言文学2302班','123456aA!','13800138004',65.20),('2023030301','孙七','医学院','临床医学2303班','123456aA!','13800138005',88.90),('2023030302','周八','医学院','临床医学2303班','123456aA!','13800138006',72.30),('2023040401','吴九','商学院','财务管理2304班','123456aA!','13800138007',90.10),('2023040402','郑十','商学院','财务管理2304班','123456aA!','13800138008',68.40),('2023050501','钱一','外国语学院','英语2305班','123456aA!','13800138009',81.60),('2023050502','冯二','外国语学院','英语2305班','123456aA!','13800138010',79.80);
/*!40000 ALTER TABLE `student` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `teacher`
--

DROP TABLE IF EXISTS `teacher`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `teacher` (
  `account` varchar(50) COLLATE utf8mb4_general_ci NOT NULL COMMENT '老师账号',
  `password` varchar(255) COLLATE utf8mb4_general_ci NOT NULL COMMENT '密码',
  `teacher_name` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '老师姓名',
  `office_location` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '办公地点',
  `phone` varchar(20) COLLATE utf8mb4_general_ci NOT NULL COMMENT '手机号',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '1启用 0停用',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`account`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='教师表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `teacher`
--

LOCK TABLES `teacher` WRITE;
/*!40000 ALTER TABLE `teacher` DISABLE KEYS */;
INSERT INTO `teacher` VALUES ('teacher01','teacher@01','张老师','计算机学院2号楼301室','13900139001',1,'2026-03-15 02:29:57','2026-03-15 02:29:57'),('teacher02','teacher@02','李老师','文学院1号楼405室','13900139002',1,'2026-03-15 02:29:57','2026-03-15 02:29:57'),('teacher03','teacher@03','王老师','医学院3号楼208室','13900139003',1,'2026-03-15 02:29:57','2026-03-15 02:29:57'),('teacher04','teacher@04','赵老师','商学院4号楼102室','13900139004',1,'2026-03-15 02:29:57','2026-03-15 02:29:57'),('teacher05','teacher@05','陈老师','外国语学院5号楼503室','13900139005',1,'2026-03-15 02:29:57','2026-03-15 02:29:57');
/*!40000 ALTER TABLE `teacher` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `teacher_schedule`
--

DROP TABLE IF EXISTS `teacher_schedule`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `teacher_schedule` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `teacher_account` varchar(50) COLLATE utf8mb4_general_ci NOT NULL COMMENT '心理老师账号',
  `week_day` tinyint NOT NULL COMMENT '星期几：1周一 2周二 3周三 4周四 5周五 6周六 7周日',
  `start_time` time NOT NULL COMMENT '开始时间',
  `end_time` time NOT NULL COMMENT '结束时间',
  `max_appointments` int NOT NULL DEFAULT '1' COMMENT '该时段最大可预约人数',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '1启用 0停用',
  `remark` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_teacher_schedule` (`teacher_account`,`week_day`,`start_time`,`end_time`),
  KEY `idx_teacher_schedule_teacher_account` (`teacher_account`),
  KEY `idx_teacher_schedule_week_day` (`week_day`),
  CONSTRAINT `fk_teacher_schedule_teacher` FOREIGN KEY (`teacher_account`) REFERENCES `teacher` (`account`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='心理老师办公时间表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `teacher_schedule`
--

LOCK TABLES `teacher_schedule` WRITE;
/*!40000 ALTER TABLE `teacher_schedule` DISABLE KEYS */;
INSERT INTO `teacher_schedule` VALUES (1,'teacher01',1,'14:00:00','17:00:00',3,1,'周一下午接待','2026-03-15 02:29:57','2026-03-15 02:29:57'),(2,'teacher01',3,'09:00:00','11:00:00',2,1,'周三上午接待','2026-03-15 02:29:57','2026-03-15 02:29:57'),(3,'teacher02',2,'15:00:00','17:00:00',2,1,'周二下午接待','2026-03-15 02:29:57','2026-03-15 02:29:57'),(4,'teacher02',4,'09:00:00','12:00:00',3,1,'周四上午接待','2026-03-15 02:29:57','2026-03-15 02:29:57'),(5,'teacher03',5,'14:30:00','17:30:00',2,1,'周五下午接待','2026-03-15 02:29:57','2026-03-15 02:29:57');
/*!40000 ALTER TABLE `teacher_schedule` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'mental_health'
--

--
-- Dumping routines for database 'mental_health'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-03-15 11:04:37
