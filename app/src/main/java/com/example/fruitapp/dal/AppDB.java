package com.example.fruitapp.dal;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.studentmanager.entities.Category;
import com.example.studentmanager.entities.Order;
import com.example.studentmanager.entities.OrderDetail;
import com.example.studentmanager.entities.Product;
import com.example.studentmanager.entities.User;

@Database(
    entities = {User.class, Category.class, Product.class, Order.class, OrderDetail.class},
    version = 5
)
public abstract class AppDB extends RoomDatabase {
    public abstract UserDAO userDAO();
    public abstract CategoryDAO categoryDAO();
    public abstract ProductDAO productDAO();
    public abstract OrderDAO orderDAO();
    public abstract OrderDetailDAO orderDetailDAO();

    private static AppDB INSTANCE;

    public static AppDB getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(
                    context.getApplicationContext(),
                    AppDB.class,
                    "ShopDB"
            )
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build();
            seedData(INSTANCE);
        }
        return INSTANCE;
    }

    private static void seedData(AppDB db) {
        if (db.userDAO().count() == 0) {
            db.userDAO().insert(new User("admin", "admin123", "Quản Trị Viên", "admin@fruitshop.vn"));
            db.userDAO().insert(new User("an", "an123456", "Nguyễn Văn An", "an.nguyen@fruitshop.vn"));
            db.userDAO().insert(new User("binh", "binh123456", "Trần Thị Bình", "binh.tran@fruitshop.vn"));
        }

        if (db.categoryDAO().count() == 0) {
            db.categoryDAO().insert(new Category("Trái Cây Nhiệt Đới", "Xoài, dứa, đu đủ, mít, chôm chôm, măng cụt"));
            db.categoryDAO().insert(new Category("Trái Cây Có Múi", "Cam, quýt, bưởi, chanh tươi các loại"));
            db.categoryDAO().insert(new Category("Trái Cây Ôn Đới", "Táo, lê, nho, dâu tây nhập khẩu và nội địa"));
            db.categoryDAO().insert(new Category("Đặc Sản Việt Nam", "Sầu riêng, thanh long, vải thiều, nhãn lồng"));
            db.categoryDAO().insert(new Category("Trái Cây Nhập Khẩu", "Cherry, kiwi, việt quất, lựu từ Mỹ, Úc, Hàn Quốc"));
        }

        if (db.productDAO().count() == 0) {
            // Trái Cây Nhiệt Đới (categoryId = 1)
            db.productDAO().insert(new Product("Xoài Cát Hòa Lộc (1kg)", "Xoài cát Hòa Lộc đặc sản Tiền Giang, thịt dày, ngọt thơm, không xơ", 55000, 100, 1));
            db.productDAO().insert(new Product("Dứa Đồng Nai (1 trái)", "Dứa Queen Đồng Nai, trái to đều, vị ngọt chua hài hòa, thơm đặc trưng", 25000, 80, 1));
            db.productDAO().insert(new Product("Đu Đủ Đài Loan (1kg)", "Đu đủ Đài Loan ruột đỏ, ngọt mềm, giàu vitamin C và enzyme tiêu hóa", 30000, 60, 1));
            db.productDAO().insert(new Product("Mít Thái Ruột Vàng (1kg)", "Mít Thái múi dày, giòn ngọt, ít xơ, thu hoạch tươi từ vườn miền Nam", 35000, 50, 1));
            // Trái Cây Có Múi (categoryId = 2)
            db.productDAO().insert(new Product("Cam Sành Vĩnh Long (1kg)", "Cam sành Vĩnh Long vỏ xanh mỏng, tép mọng nước, vị ngọt chua tự nhiên", 40000, 150, 2));
            db.productDAO().insert(new Product("Bưởi Da Xanh Bến Tre (1 trái)", "Bưởi da xanh Bến Tre loại 1, múi hồng, vị ngọt nhẹ, ít hạt, thơm mát", 75000, 70, 2));
            db.productDAO().insert(new Product("Quýt Đường Lai Vung (1kg)", "Quýt đường Lai Vung - Đồng Tháp, tép mọng đậm ngọt, thơm mùi đặc trưng", 50000, 90, 2));
            db.productDAO().insert(new Product("Chanh Vàng Không Hạt (500g)", "Chanh vàng không hạt nhập khẩu, nhiều nước, vị chua thanh, dùng pha đồ uống", 35000, 200, 2));
            // Trái Cây Ôn Đới (categoryId = 3)
            db.productDAO().insert(new Product("Táo Fuji Nhật Bản (1kg)", "Táo Fuji Nhật Bản loại 1, vỏ đỏ bóng, thịt giòn ngọt, hàng nhập khẩu chính hãng", 120000, 80, 3));
            db.productDAO().insert(new Product("Nho Xanh Mỹ Không Hạt (500g)", "Nho xanh không hạt nhập Mỹ, hạt mọng nước, vị ngọt mát dịu, da mỏng", 130000, 40, 3));
            db.productDAO().insert(new Product("Dâu Tây Đà Lạt (250g)", "Dâu tây Đà Lạt tươi ngon, trái to đỏ đều, vị ngọt chua tự nhiên, hái ngày", 55000, 60, 3));
            db.productDAO().insert(new Product("Lê Hàn Quốc (1kg)", "Lê Hàn Quốc vàng to tròn, thịt giòn mịn, rất ngọt, bảo quản tốt", 120000, 55, 3));
            // Đặc Sản Việt Nam (categoryId = 4)
            db.productDAO().insert(new Product("Sầu Riêng Monthong (1kg)", "Sầu riêng Monthong Thái trồng tại Bình Phước, cơm vàng dày, hạt lép, béo ngậy", 180000, 30, 4));
            db.productDAO().insert(new Product("Thanh Long Ruột Đỏ (1kg)", "Thanh long ruột đỏ Long An, vị ngọt đậm hơn thanh long trắng, nhiều vitamin", 45000, 100, 4));
            db.productDAO().insert(new Product("Vải Thiều Lục Ngạn (1kg)", "Vải thiều Lục Ngạn - Bắc Giang, hạt nhỏ, cùi dày, vị ngọt thơm đặc sắc", 65000, 70, 4));
            db.productDAO().insert(new Product("Nhãn Lồng Hưng Yên (1kg)", "Nhãn lồng Hưng Yên đặc sản, cùi dày giòn, vị ngọt đậm, thơm lừng", 70000, 80, 4));
            // Trái Cây Nhập Khẩu (categoryId = 5)
            db.productDAO().insert(new Product("Cherry Đỏ Mỹ (500g)", "Cherry đỏ Washington Mỹ, trái to mọng, vị ngọt pha chút chua, nhập khẩu tươi", 280000, 25, 5));
            db.productDAO().insert(new Product("Kiwi New Zealand (1kg)", "Kiwi Zespri New Zealand xanh, ruột xanh mịn, giàu vitamin C, vị chua ngọt", 130000, 60, 5));
            db.productDAO().insert(new Product("Việt Quất Mỹ (125g)", "Việt quất Blueberry Mỹ tươi, giàu chất chống oxy hóa, tốt cho sức khỏe tim mạch", 95000, 45, 5));
            db.productDAO().insert(new Product("Lựu Ai Cập (1kg)", "Lựu đỏ Ai Cập nhập khẩu, hạt to mọng đỏ, vị ngọt đậm, tốt cho hệ miễn dịch", 95000, 50, 5));
        }
    }
}
