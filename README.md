# MinLish App - Ứng dụng hỗ trợ học từ vựng tiếng Anh

## 📖 Giới thiệu

**MinLish App** là ứng dụng Android hỗ trợ học từ vựng tiếng Anh thông qua các phương pháp học hiện đại:

- 📚 Flashcard Learning
- 🧠 Spaced Repetition System (SRS)
- 📝 Context-based Learning

Ứng dụng giúp người học ghi nhớ từ vựng hiệu quả, theo dõi tiến độ học tập và xây dựng thói quen học mỗi ngày.

---

# 🎯 Mục tiêu

Xây dựng ứng dụng Android hỗ trợ học từ vựng:

- Học nhanh nhớ lâu
- Ôn tập thông minh bằng thuật toán SRS
- Theo dõi tiến độ học tập
- UI/UX đơn giản, dễ sử dụng

---

# 👥 Đối tượng người dùng

- Học sinh, sinh viên
- Người học IELTS / TOEIC
- Người đi làm
- Người muốn cải thiện vốn từ vựng tiếng Anh

---

# 🚀 Chức năng chính

## 1. Authentication

### 🔐 Đăng ký / Đăng nhập

- Email + Password
- Google Sign-In

### 👤 Hồ sơ người dùng

- Tên người dùng
- Mục tiêu học tập
- Trình độ tiếng Anh (A1 → C2)

---

## 2. Vocabulary Management

### 📂 Quản lý bộ từ vựng

- Tạo bộ từ
- Chỉnh sửa / xóa bộ từ
- Gắn tag:
  - IELTS
  - TOEIC
  - Business
  - Travel

### 📖 Quản lý từ vựng

Mỗi từ vựng bao gồm:

- Word
- Pronunciation
- Meaning
- Description
- Example
- Collocation
- Related Words
- Note

### 📥 Import / Export

- Import CSV / Excel
- Export bộ từ vựng

---

## 3. Learning Module

### 🃏 Flashcard

- Hiển thị từ ở mặt trước
- Nghĩa + ví dụ ở mặt sau
- Flip animation

### 🔁 Spaced Repetition (SM-2)

Người dùng đánh giá mức độ nhớ:

- Again
- Hard
- Good
- Easy

Hệ thống tính toán:

- Ease Factor
- Interval
- Next Review Time

### 📅 Daily Learning Plan

- Số từ mới mỗi ngày
- Số từ cần ôn tập
- Kế hoạch học tự động

---

## 4. Progress Tracking

### 📊 Dashboard

- Tổng số từ đã học
- Accuracy
- Learning streak

### 📈 Statistics

- Daily Activity
- Retention Rate
- Learning Progress

### 🏆 Level Estimation

- Beginner
- Intermediate
- Advanced

---

## 5. Notification System

- Nhắc học mỗi ngày
- Nhắc ôn tập từ đến hạn
- Push Notification

---

# 🧩 Kiến trúc hệ thống

## Android Architecture

- MVVM Architecture
- Repository Pattern
- Clean Architecture

---

# 🛠️ Công nghệ sử dụng

## Android

- Kotlin
- Jetpack Compose / XML
- Android Jetpack

## Architecture Components

- ViewModel
- LiveData / StateFlow
- Navigation Component
- Room Database

## Backend / Cloud

- Firebase Authentication
- Firebase Firestore
- Firebase Cloud Messaging

## Local Storage

- Room Database
- DataStore

## Dependency Injection

- Hilt / Dagger

---

# ⚙️ Yêu cầu phi chức năng

## 🚀 Performance

- Thời gian tải < 2s
- Tối ưu cho thiết bị Android tầm trung

## 🔒 Security

- JWT Authentication
- Password Encryption (bcrypt)

## 🎨 UI/UX

- Giao diện đơn giản
- Dễ sử dụng
- Dark Mode support

---

# 📌 Định hướng phát triển

- AI gợi ý từ vựng
- Speech pronunciation checking
- Widget học từ vựng
- Đồng bộ đa thiết bị
- Offline Mode

---

# 📄 License

This project is developed for educational and learning purposes.
