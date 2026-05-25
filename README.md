# MinLish App - Ứng dụng hỗ trợ học từ vựng tiếng Anh

## 📖 Giới thiệu

**MinLish App** là ứng dụng hỗ trợ học từ vựng tiếng Anh hiện đại, giúp người học ghi nhớ từ vựng hiệu quả thông qua các phương pháp:

- 📚 Flashcard Learning
- 🧠 Spaced Repetition System (SRS)
- 📝 Context-based Learning

Ứng dụng phù hợp cho học sinh, sinh viên, người học IELTS/TOEIC và người đi làm muốn nâng cao vốn từ vựng tiếng Anh.

---

# 🎯 Mục tiêu

Xây dựng nền tảng học từ vựng:

- Dễ sử dụng
- Học nhanh nhớ lâu
- Theo dõi tiến độ học tập
- Tự động nhắc ôn tập thông minh

---

# 🚀 Tính năng chính

## 1. User Management

### 🔐 Đăng ký / Đăng nhập

- Đăng nhập bằng Email + Password
- Google Login

### 👤 Hồ sơ người dùng

- Tên người dùng
- Mục tiêu học tập (IELTS, TOEIC, giao tiếp,…)
- Trình độ tiếng Anh (A1 → C2)

---

## 2. Vocabulary Management

### 📂 Tạo bộ từ vựng

Người dùng có thể:

- Tạo bộ từ riêng
- Thêm mô tả
- Gắn tags:
  - IELTS
  - Business
  - Travel
  - Academic

### 📖 Quản lý từ vựng

Mỗi từ vựng bao gồm:

- Word
- Pronunciation
- Meaning
- Description (English)
- Example sentence
- Collocation
- Related words
- Note

### 📥 Import / Export

- Import từ file CSV / Excel
- Export bộ từ vựng

---

## 3. Learning Module

### 🃏 Flashcard Learning

- Front side: Word
- Back side: Meaning + Example
- Flip animation trực quan

### 🔁 Spaced Repetition System (SM-2)

Ứng dụng áp dụng thuật toán **SM-2**.

Người dùng đánh giá mức độ nhớ:

- Again
- Hard
- Good
- Easy

Hệ thống sẽ tự động tính:

- Next review time
- Ease factor

### 📅 Daily Learning Plan

- Số từ mới mỗi ngày
- Số từ cần ôn tập
- Kế hoạch học thông minh

---

## 4. Progress Tracking

### 📊 Dashboard

Hiển thị:

- Tổng số từ đã học
- Learning streak
- Accuracy (% đúng)

### 📈 Biểu đồ thống kê

- Daily activity
- Retention rate

### 🏆 Level Estimation

Đánh giá trình độ:

- Beginner
- Intermediate
- Advanced

---

## 5. Notification System

- Nhắc học mỗi ngày
- Nhắc từ đến hạn ôn tập
- Email notification
- Push notification

---

# 🧩 Các Module Chính

1. User Management
2. Vocabulary Management
3. Learning Engine (SRS)
4. Practice Module
5. Analytics & Progress
6. Notification System

---

# ⚙️ Yêu cầu phi chức năng

## 🚀 Performance

- Thời gian tải < 2s
- Hỗ trợ ~1000 concurrent users

## 🔒 Security

- JWT Authentication
- Password Encryption (bcrypt)

## 🎨 Usability

- UI/UX đơn giản
- Dễ sử dụng
- Tối ưu trải nghiệm học tập

---

# 👥 Đối tượng người dùng

- Học sinh, sinh viên
- Người học IELTS / TOEIC
- Người đi làm
- Người muốn cải thiện vốn từ vựng tiếng Anh

---

# 🛠️ Công nghệ đề xuất

## Frontend

- ReactJS / NextJS
- TailwindCSS

## Backend

- NodeJS + ExpressJS
- Spring Boot (optional)

## Database

- PostgreSQL / MongoDB

## Authentication

- JWT
- OAuth2 Google Login

## Notification

- Firebase Cloud Messaging
- Email Service

---

# 📌 Định hướng phát triển

- AI gợi ý từ vựng phù hợp
- Speech pronunciation checking
- Multiplayer vocabulary challenge
- Mobile App (Android / iOS)

---

# 📄 License

This project is developed for educational and learning purposes.