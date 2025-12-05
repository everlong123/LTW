# Hướng Dẫn Đẩy Project Lên GitHub

## 📋 Các bước thực hiện:

### 1. Khởi tạo Git Repository

```bash
cd demo
git init
```

### 2. Kiểm tra .gitignore

Đảm bảo file `.gitignore` có các nội dung sau để không commit các file không cần thiết:

```
# Build files
build/
bin/
out/
*.class
*.jar
*.war

# IDE
.idea/
*.iml
.vscode/
*.swp
*.swo

# OS
.DS_Store
Thumbs.db

# Logs
*.log

# Application
application-local.properties
```

### 3. Thêm tất cả files

```bash
git add .
```

### 4. Commit lần đầu

```bash
git commit -m "Initial commit: Tour Management System"
```

### 5. Thêm Remote Repository

```bash
git remote add origin https://github.com/everlong123/LTW.git
```

### 6. Đổi tên branch chính (nếu cần)

```bash
git branch -M main
```

### 7. Push lên GitHub

```bash
git push -u origin main
```

---

## 🔐 Nếu gặp lỗi Authentication:

### Cách 1: Sử dụng Personal Access Token

1. Vào GitHub → Settings → Developer settings → Personal access tokens → Tokens (classic)
2. Tạo token mới với quyền `repo`
3. Copy token
4. Khi push, dùng token thay vì password:
   ```
   Username: everlong123
   Password: [paste token here]
   ```

### Cách 2: Sử dụng SSH (khuyến nghị)

1. Tạo SSH key:
   ```bash
   ssh-keygen -t ed25519 -C "your_email@example.com"
   ```

2. Copy public key:
   ```bash
   cat ~/.ssh/id_ed25519.pub
   ```

3. Thêm vào GitHub: Settings → SSH and GPG keys → New SSH key

4. Đổi remote URL:
   ```bash
   git remote set-url origin git@github.com:everlong123/LTW.git
   ```

---

## 📝 Lệnh đầy đủ (copy-paste):

```bash
cd demo
git init
git add .
git commit -m "Initial commit: Tour Management System"
git branch -M main
git remote add origin https://github.com/everlong123/LTW.git
git push -u origin main
```

---

## ⚠️ Lưu ý:

- **KHÔNG commit:** `application.properties` nếu có thông tin nhạy cảm (password database, email password)
- **NÊN commit:** Source code, templates, CSS, README
- Repository hiện tại đang trống, nên push sẽ thành công ngay

---

## 🔄 Cập nhật sau này:

```bash
git add .
git commit -m "Mô tả thay đổi"
git push
```

