# FruitShop — Ứng dụng Quản Lý Bán Hàng Hoa Quả

Ứng dụng Android quản lý bán hàng hoa quả: đăng ký / đăng nhập, duyệt sản phẩm theo danh mục, thêm vào giỏ hàng và xuất hóa đơn.

## Cấu trúc thư mục chính

```
app/src/main/
├── java/com/example/studentmanager/
│   ├── MainActivity.java
│   ├── activities/         # Màn hình
│   ├── adapters/           # RecyclerView Adapter
│   ├── dal/                # Database Access Layer (Room DAO)
│   ├── entities/           # Entity / Model
│   └── utils/              # Tiện ích (SessionManager, PriceUtils)
└── res/
    ├── layout/             # File XML giao diện
    ├── drawable/           # Hình ảnh, background shape
    └── values/             # Colors, Strings, Themes
```

---

## Phân chia công việc - Nhóm 02

### Trần Quang Huy B22DCCN398 — Branch: `feature/login-register`

**Phụ trách: Đăng nhập & Đăng ký tài khoản**

| File                               | Mô tả                       |
| ---------------------------------- | --------------------------- |
| `activities/LoginActivity.java`    | Màn hình đăng nhập          |
| `activities/RegisterActivity.java` | Màn hình đăng ký            |
| `dal/UserDAO.java`                 | Truy vấn bảng users         |
| `entities/User.java`               | Entity tài khoản người dùng |
| `utils/SessionManager.java`        | Quản lý phiên đăng nhập     |
| `res/layout/activity_login.xml`    | Giao diện đăng nhập         |
| `res/layout/activity_register.xml` | Giao diện đăng ký           |

---

### Trần Quang Huy B22DCCN397 — Branch: `feature/product-cart`

**Phụ trách: Sản phẩm, Danh mục & Giỏ hàng**

| File                                     | Mô tả                             |
| ---------------------------------------- | --------------------------------- |
| `activities/ProductListActivity.java`    | Danh sách sản phẩm                |
| `activities/ProductDetailActivity.java`  | Chi tiết sản phẩm, thêm vào giỏ   |
| `activities/CategoryListActivity.java`   | Danh sách danh mục                |
| `activities/CartActivity.java`           | Giỏ hàng / Hóa đơn đang mở        |
| `adapters/ProductAdapter.java`           | Adapter danh sách sản phẩm        |
| `adapters/CategoryAdapter.java`          | Adapter danh sách danh mục        |
| `adapters/CartItemAdapter.java`          | Adapter giỏ hàng                  |
| `dal/ProductDAO.java`                    | Truy vấn bảng products            |
| `dal/CategoryDAO.java`                   | Truy vấn bảng categories          |
| `entities/Product.java`                  | Entity sản phẩm                   |
| `entities/Category.java`                 | Entity danh mục                   |
| `entities/CartItem.java`                 | Model hiển thị item trong giỏ     |
| `utils/PriceUtils.java`                  | Định dạng tiền tệ VNĐ             |
| `res/layout/activity_product_list.xml`   | Giao diện danh sách sản phẩm      |
| `res/layout/activity_product_detail.xml` | Giao diện chi tiết sản phẩm       |
| `res/layout/activity_category_list.xml`  | Giao diện danh sách danh mục      |
| `res/layout/activity_cart.xml`           | Giao diện giỏ hàng                |
| `res/layout/item_product.xml`            | Item sản phẩm trong RecyclerView  |
| `res/layout/item_category.xml`           | Item danh mục trong RecyclerView  |
| `res/layout/item_cart.xml`               | Item trong giỏ hàng               |
| `res/drawable/btn_qty_bg.xml`            | Background nút tăng/giảm số lượng |
| `res/drawable/product_placeholder.xml`   | Placeholder ảnh sản phẩm          |

---

### Đỗ Đức Cảnh B22DCCN086 — Branch: `feature/invoice-order`

**Phụ trách: Hóa đơn, Thanh toán & Cấu hình chung**

| File                              | Mô tả                              |
| --------------------------------- | ---------------------------------- |
| `MainActivity.java`               | Màn hình chính                     |
| `activities/InvoiceActivity.java` | Màn hình hóa đơn sau thanh toán    |
| `dal/AppDB.java`                  | Khởi tạo Room DB, seed dữ liệu mẫu |
| `dal/OrderDAO.java`               | Truy vấn bảng orders               |
| `dal/OrderDetailDAO.java`         | Truy vấn bảng order_details        |
| `entities/Order.java`             | Entity đơn hàng                    |
| `entities/OrderDetail.java`       | Entity chi tiết đơn hàng           |
| `res/layout/activity_main.xml`    | Giao diện màn hình chính           |
| `res/layout/activity_invoice.xml` | Giao diện hóa đơn                  |

---

## Tài khoản demo

| Tên đăng nhập | Mật khẩu   | Họ tên        |
| ------------- | ---------- | ------------- |
| admin         | admin123   | Quản Trị Viên |
| an            | an123456   | Nguyễn Văn An |
| binh          | binh123456 | Trần Thị Bình |
