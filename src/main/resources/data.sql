-- XStore Fashion - Dữ liệu mặc định cho shop bán quần áo
-- Created: 2025-11-13

-- Tắt foreign key checks để có thể xóa dữ liệu
SET FOREIGN_KEY_CHECKS = 0;

-- Xóa dữ liệu cũ (nếu có) - theo thứ tự foreign key
DELETE FROM stock_items WHERE id > 0;
DELETE FROM product_info WHERE id > 0;
DELETE FROM products WHERE id > 0;
DELETE FROM product_types WHERE id > 0;
DELETE FROM stocks WHERE id > 0;
-- Note: Không xóa comments và comment_attachments để giữ dữ liệu user

-- Bật lại foreign key checks
SET FOREIGN_KEY_CHECKS = 1;

-- Insert addresses first (required by stocks foreign key)
INSERT IGNORE INTO addresses (id, street_number, street_name, ward, district, city, is_default) VALUES
(1, '123', 'Đường Nguyễn Trãi', 'Thanh Xuân Trung', 'Thanh Xuân', 'Hà Nội', true),
(2, '456', 'Đường Nguyễn Huệ', 'Bến Nghé', 'Quận 1', 'TP.HCM', true),
(3, '789', 'Đường Trần Phú', 'Thạch Thang', 'Hải Châu', 'Đà Nẵng', true);

-- Insert sample stocks (kho hàng)
INSERT IGNORE INTO stocks (id, name, phone, email, address_id) VALUES
(1, 'Kho Miền Bắc - Hà Nội', '0243.456.789', 'hanoi@xstore.vn', 1),
(2, 'Kho Miền Nam - TP.HCM', '0283.456.789', 'hcm@xstore.vn', 2),
(3, 'Kho Miền Trung - Đà Nẵng', '0236.456.789', 'danang@xstore.vn', 3);

-- Insert product types (danh mục sản phẩm)
INSERT IGNORE INTO product_types (id, name, description) VALUES
(1, 'Áo Nam', 'Áo thun, áo sơ mi, áo khoác, áo len và các loại áo dành cho nam giới'),
(2, 'Quần Nam', 'Quần jeans, quần kaki, quần short, quần tây và các loại quần dành cho nam giới'),
(3, 'Áo Nữ', 'Áo thun, áo blouse, áo khoác, áo len và các loại áo dành cho nữ giới'),
(4, 'Quần Nữ', 'Quần jeans, chân váy, quần culottes, quần legging và các loại quần dành cho nữ giới'),
(5, 'Giày Dép', 'Giày sneaker, giày boot, sandal và các loại giày dép cho nam và nữ'),
(6, 'Phụ Kiện', 'Túi xách, nón, thắt lưng, kính mát, khăn choàng và các phụ kiện thời trang khác');

-- Insert sample products với ảnh thật từ Unsplash
INSERT IGNORE INTO products (id, name, description, image, product_type_id, brand, fabric, price_in_stock, price) VALUES
-- Áo Nam (1-3, 19-25)
(1, 'Áo Thun Nam Basic Cotton', 'Áo thun nam basic chất liệu cotton 100%, form regular fit thoải mái. Phù hợp mặc hàng ngày, đi chơi hay đi làm.', 'https://down-vn.img.susercontent.com/file/vn-11134207-7ras8-m4cr1glw33j4da.webp', 1, 'X-Store', 'Cotton 100%', 120000, 179000),

(2, 'Áo Sơ Mi Nam Công Sở', 'Áo sơ mi nam công sở chất vải cotton pha, form slim fit hiện đại. Thiết kế lịch lãm, phù hợp môi trường công sở.', 'https://down-vn.img.susercontent.com/file/vn-11134207-7r98o-ltm7exxdpgr188.webp', 1, 'X-Store', 'Cotton Blend', 280000, 399000),

(3, 'Áo Hoodie Nam Streetwear', 'Áo hoodie nam phong cách streetwear, chất nỉ cotton dày dặn. Form oversize trendy với hood và túi kangaroo.', 'https://down-vn.img.susercontent.com/file/vn-11134207-7ras8-m127pot2g1i708.webp', 1, 'X-Store', 'Cotton Fleece', 350000, 499000),

(19, 'Áo Polo Nam Classic', 'Áo polo nam cổ bẻ chất cotton pha, form regular fit. Thiết kế cổ bẻ truyền thống, phù hợp công sở và dạo phố.', 'https://down-vn.img.susercontent.com/file/vn-11134207-7ras8-m0wmpqw9qne5ea@resize_w900_nl.webp', 1, 'X-Store', 'Cotton Blend', 220000, 319000),
(20, 'Áo Len Nam Sweater', 'Áo len nam sweater cổ lọ chất acrylic ấm áp. Form oversize trendy, phù hợp mùa đông.', 'https://down-vn.img.susercontent.com/file/vn-11134207-820l4-mgrc528r3jexd7@resize_w900_nl.webp', 1, 'X-Store', 'Acrylic', 380000, 549000),
(21, 'Áo Khoác Nam Jacket', 'Áo khoác nam jacket da PU chống nước. Form bomber hiện đại, túi zip tiện lợi.', 'https://down-vn.img.susercontent.com/file/vn-11134207-820l4-mg1ryc47f6z192.webp', 1, 'X-Store', 'PU Leather', 550000, 799000),
(22, 'Áo Vest Nam Công Sở', 'Áo vest nam công sở chất vải len dạ. Thiết kế 3 khuy lịch lãm, phù hợp tiệc tùng.', 'https://down-vn.img.susercontent.com/file/vn-11134207-820l4-mhhkfh5aq9dy18@resize_w900_nl.webp', 1, 'X-Store', 'Wool Blend', 450000, 649000),
(23, 'Áo Thun Nam Graphic', 'Áo thun nam in họa tiết graphic chất cotton organic. Form relaxed fit thoải mái.', 'https://down-vn.img.susercontent.com/file/vn-11134207-820l4-mhhdkau29ou899@resize_w900_nl.webp', 1, 'X-Store', 'Organic Cotton', 160000, 239000),
(24, 'Áo Cardigan Nam', 'Áo cardigan nam len dệt kim. Form dài qua hông, khuy cài tiện lợi.', 'https://down-vn.img.susercontent.com/file/vn-11134207-7ras8-m1qp020tey7zfd@resize_w900_nl.webp', 1, 'X-Store', 'Wool', 420000, 599000),
(25, 'Áo Tank Top Nam', 'Áo tank top nam thể thao chất polyester thấm hút. Form fitted, phù hợp gym.', 'https://down-vn.img.susercontent.com/file/vn-11134207-7qukw-ljhiuwm3u8qcbc@resize_w900_nl.webp', 1, 'X-Store', 'Polyester', 120000, 179000),

-- Quần Nam (7-9, 33-36)
(7, 'Quần Jeans Nam Slim Fit', 'Quần jeans nam slim fit chất denim cotton co giãn. Wash nhẹ tự nhiên, phom dáng ôm vừa phải.', 'https://down-vn.img.susercontent.com/file/cn-11134207-7r98o-lsmc0qj01peldc.webp', 2, 'X-Store', 'Denim Cotton', 380000, 549000),

(8, 'Quần Kaki Nam Chinos', 'Quần kaki nam chinos chất cotton twill mềm mại. Form straight leg thoải mái, phù hợp nhiều dịp.', 'https://down-vn.img.susercontent.com/file/vn-11134207-7ras8-mcq08j1nqa3xf5.webp', 2, 'X-Store', 'Cotton Twill', 290000, 419000),

(9, 'Quần Short Nam Thể Thao', 'Quần short nam thể thao chất polyester thấm hút mồ hôi. Có lót bên trong và túi zip tiện lợi.', 'https://down-vn.img.susercontent.com/file/vn-11134207-820l4-mgxm6zaqj1fz0f.webp', 2, 'X-Store', 'Polyester DryFit', 180000, 259000),

(33, 'Quần Jogger Nam', 'Quần jogger nam thể thao chất cotton pha. Ống bo gấu, túi zip tiện lợi.', 'https://down-vn.img.susercontent.com/file/vn-11134207-7r98o-lufafv28l34v45@resize_w900_nl.webp', 2, 'X-Store', 'Cotton Blend', 280000, 399000),
(34, 'Quần Cargo Nam', 'Quần cargo nam nhiều túi chất cotton canvas. Form straight leg rugged.', 'https://down-vn.img.susercontent.com/file/vn-11134207-7ra0g-mae3q7fwu0ns1d@resize_w900_nl.webp', 2, 'X-Store', 'Canvas', 350000, 499000),
(35, 'Quần Tây Nam Công Sở', 'Quần tây nam công sở chất wool pha. Form slim fit lịch lãm.', 'https://down-vn.img.susercontent.com/file/vn-11134207-7r98o-lz48giwx53d980.webp', 2, 'X-Store', 'Wool Blend', 420000, 599000),
(36, 'Quần Ống Rộng Nam', 'Quần ống rộng nam chất linen thoáng mát. Form wide leg bohemian.', 'https://down-vn.img.susercontent.com/file/cn-11134207-7ras8-m299acbpffjy8a@resize_w900_nl.webp', 2, 'X-Store', 'Linen', 320000, 459000),

-- Áo Nữ (4-6, 26-32)
(4, 'Áo Thun Nữ Crop Top', 'Áo thun nữ crop top trendy chất cotton co giãn. Form fitted ôm dáng, phù hợp mix đồ năng động.', 'https://down-vn.img.susercontent.com/file/vn-11134207-7ra0g-m6ew5sbvhk8179.webp', 3, 'X-Store', 'Cotton Spandex', 140000, 199000),

(5, 'Áo Blouse Nữ Công Sở', 'Áo blouse nữ công sở chất vải lụa mềm mại. Thiết kế thanh lịch với tay bồng nhẹ, phù hợp đi làm.', 'https://down-vn.img.susercontent.com/file/vn-11134207-820l4-mft9lae7f3t9d4.webp', 3, 'X-Store', 'Polyester Silk', 320000, 459000),

(6, 'Áo Khoác Blazer Nữ', 'Áo khoác blazer nữ form fitted sang trọng. Chất vải polyester cao cấp, thiết kế 1 button hiện đại.', 'https://down-vn.img.susercontent.com/file/vn-11134207-820l4-mdvmq4oag9vrd9.webp', 3, 'X-Store', 'Polyester Premium', 450000, 649000),

(26, 'Áo Len Nữ Cardigan', 'Áo len nữ cardigan dệt kim mềm mại. Form dài midi, khuy cài trang trí.', 'https://down-vn.img.susercontent.com/file/vn-11134207-820l4-mh0qe5s5302794.webp', 3, 'X-Store', 'Acrylic', 350000, 499000),
(27, 'Áo Khoác Nữ Trench Coat', 'Áo khoác nữ trench coat chất gabardine chống nước. Thiết kế cổ đứng, dây nịt eo.', 'https://down-vn.img.susercontent.com/file/vn-11134207-820l4-mg3xd2ntou896b@resize_w900_nl.webp', 3, 'X-Store', 'Gabardine', 680000, 969000),
(28, 'Áo Thun Nữ Oversize', 'Áo thun nữ oversize chất cotton dày dặn. Form rộng rãi, tay dài.', 'https://down-vn.img.susercontent.com/file/vn-11134207-820l4-mgm344vazymmed.webp', 3, 'X-Store', 'Cotton', 180000, 259000),
(29, 'Áo Blazer Nữ Slim Fit', 'Áo blazer nữ slim fit chất polyester cao cấp. Form ôm dáng thanh lịch.', 'https://down-vn.img.susercontent.com/file/vn-11134207-820l4-mh4td7yy5vd636@resize_w900_nl.webp', 3, 'X-Store', 'Polyester', 520000, 749000),
(30, 'Áo Sweater Nữ', 'Áo sweater nữ cổ tròn chất len acrylic. Form regular, ấm áp mùa lạnh.', 'https://down-vn.img.susercontent.com/file/vn-11134207-7ras8-mbz2f0fn0atxf1@resize_w900_nl.webp', 3, 'X-Store', 'Acrylic', 320000, 459000),
(31, 'Áo Vest Nữ', 'Áo vest nữ công sở chất vải tweed. Thiết kế không khuy, form fitted.', 'https://down-vn.img.susercontent.com/file/vn-11134207-820l4-mh7g9cwo0m4pa6.webp', 3, 'X-Store', 'Tweed', 480000, 689000),
(32, 'Áo Tank Top Nữ', 'Áo tank top nữ chất modal thoáng mát. Form fitted, phù hợp layering.', 'https://down-vn.img.susercontent.com/file/vn-11134207-820l4-mgywpg5gzsp96d@resize_w900_nl.webp', 3, 'X-Store', 'Modal', 140000, 199000),

-- Quần Nữ (10-12, 37-40)
(10, 'Quần Jeans Nữ Skinny', 'Quần jeans nữ skinny fit ôm dáng hoàn hảo. Chất denim co giãn 4 chiều, tôn lên đường cong cơ thể.', 'https://down-vn.img.susercontent.com/file/vn-11134207-820l4-mh08f50loy6i88.webp', 4, 'X-Store', 'Denim Stretch', 420000, 599000),

(11, 'Chân Váy Chữ A Vintage', 'Chân váy chữ A vintage chất vải cotton pha. Thiết kế xoè nhẹ, độ dài qua gối thanh lịch.', 'https://down-vn.img.susercontent.com/file/cn-11134207-820l4-mf6oj5gpjm6if1.webp', 4, 'X-Store', 'Cotton Blend', 250000, 359000),

(12, 'Quần Culottes Nữ Wide Leg', 'Quần culottes nữ wide leg thời thượng. Chất vải polyester mềm mát, phù hợp thời tiết nóng.', 'https://down-vn.img.susercontent.com/file/vn-11134207-7ras8-m12uufuvrw9ne0.webp', 4, 'X-Store', 'Polyester Crepe', 330000, 479000),

-- Quần Nữ (37-40)
(37, 'Quần Legging Nữ', 'Quần legging nữ thể thao chất spandex co giãn. Form ôm sát, thấm hút mồ hôi.', 'https://down-vn.img.susercontent.com/file/vn-11134207-820l4-mfcfr61osahb0a@resize_w900_nl.webp', 4, 'X-Store', 'Spandex', 220000, 319000),
(38, 'Quần Tây Nữ', 'Quần tây nữ công sở chất gabardine. Form straight leg thanh lịch.', 'https://down-vn.img.susercontent.com/file/sg-11134201-7reng-m83f1sgnh9xz12@resize_w900_nl.webp', 4, 'X-Store', 'Gabardine', 380000, 549000),
(39, 'Quần Short Nữ', 'Quần short nữ chất denim co giãn. Form high waist trendy.', 'https://down-vn.img.susercontent.com/file/vn-11134207-820l4-me7dpz5b6a694b.webp', 4, 'X-Store', 'Denim', 240000, 349000),
(40, 'Quần Jogger Nữ', 'Quần jogger nữ thể thao chất polyester. Ống bo gấu, dây rút eo.', 'https://down-vn.img.susercontent.com/file/vn-11134207-820l4-mhg0853apybr4e@resize_w900_nl.webp', 4, 'X-Store', 'Polyester', 260000, 379000),

-- Giày Dép (13-15, 41-44)
(13, 'Giày Sneaker Nam Basic', 'Giày sneaker nam basic phong cách minimalist. Upper da synthetic bền bỉ, đế rubber chống trượt.', 'https://down-vn.img.susercontent.com/file/vn-11134207-820l4-mguf3n6wjg21cc.webp', 5, 'X-Store', 'Synthetic Leather', 580000, 799000),

(14, 'Giày Boot Nữ Chelsea', 'Giày boot nữ chelsea cổ điển. Chất da PU cao cấp, gót cao 5cm thanh lịch và thoải mái.', 'https://down-vn.img.susercontent.com/file/vn-11134207-820l4-mhm1htcr1s7cb4.webp', 5, 'X-Store', 'PU Leather', 650000, 899000),

(15, 'Sandal Nữ Đế Xuồng', 'Sandal nữ đế xuồng phong cách bohemian. Quai ngang mềm mại, đế cao 7cm ổn định.', 'https://down-vn.img.susercontent.com/file/vn-11134207-7ra0g-m77va6ivqveeb6.webp', 5, 'X-Store', 'Synthetic', 320000, 459000),

(41, 'Giày Lười Nam Loafers', 'Giày lười nam loafers da thật. Thiết kế penny slot cổ điển.', 'https://down-vn.img.susercontent.com/file/8d771910d124d6f7a54be8674f6e99e7@resize_w900_nl.webp', 5, 'X-Store', 'Genuine Leather', 650000, 899000),
(42, 'Giày Cao Gót Nữ Pumps', 'Giày cao gót nữ pumps da PU. Gót 8cm thanh lịch, mũi nhọn.', 'https://down-vn.img.susercontent.com/file/8e548a79b4239f1c5d742c28fccb68fb@resize_w900_nl.webp', 5, 'X-Store', 'PU Leather', 480000, 689000),
(43, 'Giày Thể Thao Unisex', 'Giày thể thao unisex chất mesh thoáng khí. Đế EVA êm ái.', 'https://down-vn.img.susercontent.com/file/vn-11134207-820l4-meeasod3ponaac.webp', 5, 'X-Store', 'Mesh', 420000, 599000),
(44, 'Sandal Nam Flip Flops', 'Sandal nam flip flops chất EVA nhẹ nhàng. Thiết kế đơn giản, dễ chịu.', 'https://down-vn.img.susercontent.com/file/vn-11134207-7r98o-lmwobtbjn3rz7e@resize_w900_nl.webp', 5, 'X-Store', 'EVA', 120000, 179000),

-- Phụ Kiện (16-18, 45-50)
(16, 'Túi Tote Canvas Unisex', 'Túi tote canvas unisex phong cách vintage. Chất canvas dày bền, size vừa phải đựng đồ hàng ngày.', 'https://down-vn.img.susercontent.com/file/sg-11134201-22110-9zkxulnuadjvf6@resize_w900_nl.webp', 6, 'X-Store', 'Canvas', 180000, 259000),

(17, 'Nón Snapback Streetwear', 'Nón snapback streetwear chất cotton twill. Logo thêu nổi 3D, viền nón cong phong cách Mỹ.', 'https://down-vn.img.susercontent.com/file/vn-11134207-7ras8-m2fgt3e3atv8bd.webp', 6, 'X-Store', 'Cotton Twill', 150000, 219000),

(18, 'Thắt Lưng Da Nam', 'Thắt lưng da nam genuine leather. Mặt khóa kim loại cao cấp, thiết kế cổ điển sang trọng.', 'https://down-vn.img.susercontent.com/file/64c36d524a632b9745d97639f4c15481.webp', 6, 'X-Store', 'Genuine Leather', 290000, 419000),

(45, 'Túi Xách Nữ Tote', 'Túi xách nữ tote canvas in họa tiết. Size lớn đựng laptop.', 'https://down-vn.img.susercontent.com/file/vn-11134207-820l4-mev5zq1qneo099@resize_w900_nl.webp', 6, 'X-Store', 'Canvas', 250000, 359000),
(46, 'Ví Nam Da', 'Ví nam da thật bifold. 6 ngăn thẻ, 2 ngăn tiền tiện lợi.', 'https://down-vn.img.susercontent.com/file/sg-11134253-7rd6w-m7hh2fcvpv9yaa@resize_w900_nl.webp', 6, 'X-Store', 'Genuine Leather', 350000, 499000),
(47, 'Kính Mát Unisex', 'Kính mát unisex chất acetate. Thiết kế wayfarer cổ điển.', 'https://down-vn.img.susercontent.com/file/vn-11134207-7r98o-luqg4zwmkc2acb@resize_w900_nl.webp', 6, 'X-Store', 'Acetate', 280000, 399000),
(48, 'Khăn Choàng Nữ Scarf', 'Khăn choàng nữ scarf lụa mềm mại. Họa tiết hoa văn tinh tế.', 'https://down-vn.img.susercontent.com/file/vn-11134207-7r98o-lzfnwseyqk6934@resize_w900_nl.webp', 6, 'X-Store', 'Silk', 220000, 319000),
(49, 'Vòng Tay Nữ Bracelet', 'Vòng tay nữ bracelet da thắt nút. Thiết kế minimalist.', 'https://down-vn.img.susercontent.com/file/vn-11134207-820l4-mglqrdmjrabx33@resize_w900_nl.webp', 6, 'X-Store', 'Leather', 150000, 219000),
(50, 'Mũ Lưỡi Trai Unisex', 'Mũ lưỡi trai unisex cotton. Thiết kế baseball cap phong cách Mỹ.', 'https://down-vn.img.susercontent.com/file/vn-11134207-7ras8-m1aoezd82qzgb0@resize_w900_nl.webp', 6, 'X-Store', 'Cotton', 180000, 259000);

-- Insert product_info (thông tin biến thể: color, size, quantity)
INSERT IGNORE INTO product_info (id, product_id, color_name, color_hex_code, size_name, image) VALUES
-- Áo Thun Nam Basic (Product 1) - 4 colors x 4 sizes = 16 variants
(1, 1, 'Trắng', '#FFFFFF', 'S', 'https://down-vn.img.susercontent.com/file/vn-11134207-7ras8-m4cr2wztfaa7f3.webp'), (2, 1, 'Trắng', '#FFFFFF', 'M', 'https://down-vn.img.susercontent.com/file/vn-11134207-7ras8-m4cr2wztfaa7f3.webp'), (3, 1, 'Trắng', '#FFFFFF', 'L', 'https://down-vn.img.susercontent.com/file/vn-11134207-7ras8-m4cr2wztfaa7f3.webp'), (4, 1, 'Trắng', '#FFFFFF', 'XL', 'https://down-vn.img.susercontent.com/file/vn-11134207-7ras8-m4cr2wztfaa7f3.webp'),
(5, 1, 'Đen', '#000000', 'S', 'https://down-vn.img.susercontent.com/file/vn-11134207-7ras8-m4cr2qngpfvkbe.webp'), (6, 1, 'Đen', '#000000', 'M', 'https://down-vn.img.susercontent.com/file/vn-11134207-7ras8-m4cr2qngpfvkbe.webp'), (7, 1, 'Đen', '#000000', 'L', 'https://down-vn.img.susercontent.com/file/vn-11134207-7ras8-m4cr2qngpfvkbe.webp'), (8, 1, 'Đen', '#000000', 'XL', 'https://down-vn.img.susercontent.com/file/vn-11134207-7ras8-m4cr2qngpfvkbe.webp'),
(9, 1, 'Xám', '#6B7280', 'S', 'https://down-vn.img.susercontent.com/file/vn-11134207-7ras8-m4cr3bvvmtov0e.webp'), (10, 1, 'Xám', '#6B7280', 'M', 'https://down-vn.img.susercontent.com/file/vn-11134207-7ras8-m4cr3bvvmtov0e.webp'), (11, 1, 'Xám', '#6B7280', 'L', 'https://down-vn.img.susercontent.com/file/vn-11134207-7ras8-m4cr3bvvmtov0e.webp'), (12, 1, 'Xám', '#6B7280', 'XL', 'https://down-vn.img.susercontent.com/file/vn-11134207-7ras8-m4cr3bvvmtov0e.webp'),
(13, 1, 'Navy', '#1E3A8A', 'S', 'https://down-vn.img.susercontent.com/file/vn-11134207-820l4-metvjnogaeiu26.webp'), (14, 1, 'Navy', '#1E3A8A', 'M', 'https://down-vn.img.susercontent.com/file/vn-11134207-820l4-metvjnogaeiu26.webp'), (15, 1, 'Navy', '#1E3A8A', 'L', 'https://down-vn.img.susercontent.com/file/vn-11134207-820l4-metvjnogaeiu26.webp'), (16, 1, 'Navy', '#1E3A8A', 'XL', 'https://down-vn.img.susercontent.com/file/vn-11134207-820l4-metvjnogaeiu26.webp'),

-- Áo Sơ Mi Nam (Product 2) - 3 colors x 4 sizes = 12 variants
(17, 2, 'Trắng', '#FFFFFF', 'S', 'https://down-vn.img.susercontent.com/file/vn-11134207-7r98o-lubxs5fiukghab@resize_w900_nl.webp'), (18, 2, 'Trắng', '#FFFFFF', 'M', 'https://down-vn.img.susercontent.com/file/vn-11134207-7r98o-lubxs5fiukghab@resize_w900_nl.webp'), (19, 2, 'Trắng', '#FFFFFF', 'L', 'https://down-vn.img.susercontent.com/file/vn-11134207-7r98o-lubxs5fiukghab@resize_w900_nl.webp'), (20, 2, 'Trắng', '#FFFFFF', 'XL', 'https://down-vn.img.susercontent.com/file/vn-11134207-7r98o-lubxs5fiukghab@resize_w900_nl.webp'),
(21, 2, 'Xanh Nhạt', '#DBEAFE', 'S', 'https://down-vn.img.susercontent.com/file/vn-11134207-7r98o-ltxn1skqf8kfd7@resize_w900_nl.webp'), (22, 2, 'Xanh Nhạt', '#DBEAFE', 'M', 'https://down-vn.img.susercontent.com/file/vn-11134207-7r98o-ltxn1skqf8kfd7@resize_w900_nl.webp'), (23, 2, 'Xanh Nhạt', '#DBEAFE', 'L', 'https://down-vn.img.susercontent.com/file/vn-11134207-7r98o-ltxn1skqf8kfd7@resize_w900_nl.webp'), (24, 2, 'Xanh Nhạt', '#DBEAFE', 'XL', 'https://down-vn.img.susercontent.com/file/vn-11134207-7r98o-ltxn1skqf8kfd7@resize_w900_nl.webp'),
(25, 2, 'Hồng Nhạt', '#FCE7F3', 'S', 'https://down-vn.img.susercontent.com/file/vn-11134207-7ras8-mck0zsrnf4ma43@resize_w900_nl.webp'), (26, 2, 'Hồng Nhạt', '#FCE7F3', 'M', 'https://down-vn.img.susercontent.com/file/vn-11134207-7ras8-mck0zsrnf4ma43@resize_w900_nl.webp'), (27, 2, 'Hồng Nhạt', '#FCE7F3', 'L', 'https://down-vn.img.susercontent.com/file/vn-11134207-7ras8-mck0zsrnf4ma43@resize_w900_nl.webp'), (28, 2, 'Hồng Nhạt', '#FCE7F3', 'XL', 'https://down-vn.img.susercontent.com/file/vn-11134207-7ras8-mck0zsrnf4ma43@resize_w900_nl.webp'),

-- Áo Hoodie Nam (Product 3) - 3 colors x 4 sizes = 12 variants
(29, 3, 'Đen', '#000000', 'S', 'https://down-vn.img.susercontent.com/file/vn-11134258-820l4-mhkj803ulerk39'), (30, 3, 'Đen', '#000000', 'M', 'https://down-vn.img.susercontent.com/file/vn-11134258-820l4-mhkj803ulerk39'), (31, 3, 'Đen', '#000000', 'L', 'https://down-vn.img.susercontent.com/file/vn-11134258-820l4-mhkj803ulerk39'), (32, 3, 'Đen', '#000000', 'XL', 'https://down-vn.img.susercontent.com/file/vn-11134258-820l4-mhkj803ulerk39'),
(33, 3, 'Xám', '#6B7280', 'S', 'https://down-vn.img.susercontent.com/file/vn-11134258-820l4-mhkj803ulerk39'), (34, 3, 'Xám', '#6B7280', 'M', 'https://down-vn.img.susercontent.com/file/vn-11134258-820l4-mhkj803ulerk39'), (35, 3, 'Xám', '#6B7280', 'L', 'https://down-vn.img.susercontent.com/file/vn-11134258-820l4-mhkj803ulerk39'), (36, 3, 'Xám', '#6B7280', 'XL', 'https://down-vn.img.susercontent.com/file/vn-11134258-820l4-mhkj803ulerk39'),
(37, 3, 'Navy', '#1E3A8A', 'S', 'https://down-vn.img.susercontent.com/file/vn-11134258-820l4-mhkj803ulerk39'), (38, 3, 'Navy', '#1E3A8A', 'M', 'https://down-vn.img.susercontent.com/file/vn-11134258-820l4-mhkj803ulerk39'), (39, 3, 'Navy', '#1E3A8A', 'L', 'https://down-vn.img.susercontent.com/file/vn-11134258-820l4-mhkj803ulerk39'), (40, 3, 'Navy', '#1E3A8A', 'XL', 'https://down-vn.img.susercontent.com/file/vn-11134258-820l4-mhkj803ulerk39'),

-- Áo Thun Nữ Crop Top (Product 4) - 3 colors x 3 sizes = 9 variants
(41, 4, 'Trắng', '#FFFFFF', 'S', 'https://down-vn.img.susercontent.com/file/vn-11134207-7ra0g-m6ew5sowx2n52a.webp'), (42, 4, 'Trắng', '#FFFFFF', 'M', 'https://down-vn.img.susercontent.com/file/vn-11134207-7ra0g-m6ew5sowx2n52a.webp'), (43, 4, 'Trắng', '#FFFFFF', 'L', 'https://down-vn.img.susercontent.com/file/vn-11134207-7ra0g-m6ew5sowx2n52a.webp'),
(44, 4, 'Hồng', '#EC4899', 'S', 'https://down-vn.img.susercontent.com/file/vn-11134207-7ra0g-m6ew5sn8zhpj44.webp'), (45, 4, 'Hồng', '#EC4899', 'M', 'https://down-vn.img.susercontent.com/file/vn-11134207-7ra0g-m6ew5sn8zhpj44.webp'), (46, 4, 'Hồng', '#EC4899', 'L', 'https://down-vn.img.susercontent.com/file/vn-11134207-7ra0g-m6ew5sn8zhpj44.webp'),
(47, 4, 'Đen', '#F59E0B', 'S', 'https://down-vn.img.susercontent.com/file/vn-11134207-7ra0g-m6ew5sbviyshee.webp'), (48, 4, 'Đen', '#F59E0B', 'M', 'https://down-vn.img.susercontent.com/file/vn-11134207-7ra0g-m6ew5sbviyshee.webp'), (49, 4, 'Đen', '#F59E0B', 'L', 'https://down-vn.img.susercontent.com/file/vn-11134207-7ra0g-m6ew5sbviyshee.webp'),

-- Áo Blouse Nữ (Product 5) - 3 colors x 3 sizes = 9 variants
(50, 5, 'Trắng', '#FFFFFF', 'S', 'https://down-vn.img.susercontent.com/file/vn-11134207-820l4-mgtq570rk6by61.webp'), (51, 5, 'Trắng', '#FFFFFF', 'M', null), (52, 5, 'Trắng', '#FFFFFF', 'L', null),
(53, 5, 'Be', '#F3E8FF', 'S', null), (54, 5, 'Be', '#F3E8FF', 'M', null), (55, 5, 'Be', '#F3E8FF', 'L', null),
(56, 5, 'Xanh Mint', '#A7F3D0', 'S', null), (57, 5, 'Xanh Mint', '#A7F3D0', 'M', null), (58, 5, 'Xanh Mint', '#A7F3D0', 'L', null),

-- Áo Khoác Blazer Nữ (Product 6) - 3 colors x 3 sizes = 9 variants
(59, 6, 'Đen', '#000000', 'S', null), (60, 6, 'Đen', '#000000', 'M', null), (61, 6, 'Đen', '#000000', 'L', null),
(62, 6, 'Navy', '#1E3A8A', 'S', null), (63, 6, 'Navy', '#1E3A8A', 'M', null), (64, 6, 'Navy', '#1E3A8A', 'L', null),
(65, 6, 'Xám', '#6B7280', 'S', null), (66, 6, 'Xám', '#6B7280', 'M', null), (67, 6, 'Xám', '#6B7280', 'L', null),

-- Quần Jeans Nam (Product 7) - 3 colors x 6 sizes = 18 variants
(68, 7, 'Xanh Đậm', '#1E40AF', '28', null), (69, 7, 'Xanh Đậm', '#1E40AF', '29', null), (70, 7, 'Xanh Đậm', '#1E40AF', '30', null), (71, 7, 'Xanh Đậm', '#1E40AF', '31', null), (72, 7, 'Xanh Đậm', '#1E40AF', '32', null), (73, 7, 'Xanh Đậm', '#1E40AF', '34', null),
(74, 7, 'Xanh Nhạt', '#3B82F6', '28', null), (75, 7, 'Xanh Nhạt', '#3B82F6', '29', null), (76, 7, 'Xanh Nhạt', '#3B82F6', '30', null), (77, 7, 'Xanh Nhạt', '#3B82F6', '31', null), (78, 7, 'Xanh Nhạt', '#3B82F6', '32', null), (79, 7, 'Xanh Nhạt', '#3B82F6', '34', null),
(80, 7, 'Đen', '#000000', '28', null), (81, 7, 'Đen', '#000000', '29', null), (82, 7, 'Đen', '#000000', '30', null), (83, 7, 'Đen', '#000000', '31', null), (84, 7, 'Đen', '#000000', '32', null), (85, 7, 'Đen', '#000000', '34', null),

-- Quần Kaki Nam (Product 8) - 3 colors x 5 sizes = 15 variants
(86, 8, 'Be', '#D97706', '28', null), (87, 8, 'Be', '#D97706', '29', null), (88, 8, 'Be', '#D97706', '30', null), (89, 8, 'Be', '#D97706', '32', null), (90, 8, 'Be', '#D97706', '34', null),
(91, 8, 'Xám', '#6B7280', '28', null), (92, 8, 'Xám', '#6B7280', '29', null), (93, 8, 'Xám', '#6B7280', '30', null), (94, 8, 'Xám', '#6B7280', '32', null), (95, 8, 'Xám', '#6B7280', '34', null),
(96, 8, 'Navy', '#1E3A8A', '28', null), (97, 8, 'Navy', '#1E3A8A', '29', null), (98, 8, 'Navy', '#1E3A8A', '30', null), (99, 8, 'Navy', '#1E3A8A', '32', null), (100, 8, 'Navy', '#1E3A8A', '34', null),

-- Quần Short Nam (Product 9) - 3 colors x 4 sizes = 12 variants
(101, 9, 'Đen', '#000000', 'S', null), (102, 9, 'Đen', '#000000', 'M', null), (103, 9, 'Đen', '#000000', 'L', null), (104, 9, 'Đen', '#000000', 'XL', null),
(105, 9, 'Xám', '#6B7280', 'S', null), (106, 9, 'Xám', '#6B7280', 'M', null), (107, 9, 'Xám', '#6B7280', 'L', null), (108, 9, 'Xám', '#6B7280', 'XL', null),
(109, 9, 'Navy', '#1E3A8A', 'S', null), (110, 9, 'Navy', '#1E3A8A', 'M', null), (111, 9, 'Navy', '#1E3A8A', 'L', null), (112, 9, 'Navy', '#1E3A8A', 'XL', null),

-- Quần Jeans Nữ (Product 10) - 3 colors x 5 sizes = 15 variants
(113, 10, 'Xanh Đậm', '#1E40AF', '25', null), (114, 10, 'Xanh Đậm', '#1E40AF', '26', null), (115, 10, 'Xanh Đậm', '#1E40AF', '27', null), (116, 10, 'Xanh Đậm', '#1E40AF', '28', null), (117, 10, 'Xanh Đậm', '#1E40AF', '29', null),
(118, 10, 'Xanh Nhạt', '#3B82F6', '25', null), (119, 10, 'Xanh Nhạt', '#3B82F6', '26', null), (120, 10, 'Xanh Nhạt', '#3B82F6', '27', null), (121, 10, 'Xanh Nhạt', '#3B82F6', '28', null), (122, 10, 'Xanh Nhạt', '#3B82F6', '29', null),
(123, 10, 'Đen', '#000000', '25', null), (124, 10, 'Đen', '#000000', '26', null), (125, 10, 'Đen', '#000000', '27', null), (126, 10, 'Đen', '#000000', '28', null), (127, 10, 'Đen', '#000000', '29', null),

-- Chân Váy (Product 11) - 3 colors x 3 sizes = 9 variants
(128, 11, 'Đen', '#000000', 'S', null), (129, 11, 'Đen', '#000000', 'M', null), (130, 11, 'Đen', '#000000', 'L', null),
(131, 11, 'Be', '#D97706', 'S', null), (132, 11, 'Be', '#D97706', 'M', null), (133, 11, 'Be', '#D97706', 'L', null),
(134, 11, 'Hồng', '#EC4899', 'S', null), (135, 11, 'Hồng', '#EC4899', 'M', null), (136, 11, 'Hồng', '#EC4899', 'L', null),

-- Quần Culottes (Product 12) - 3 colors x 3 sizes = 9 variants
(137, 12, 'Đen', '#000000', 'S', null), (138, 12, 'Đen', '#000000', 'M', null), (139, 12, 'Đen', '#000000', 'L', null),
(140, 12, 'Trắng', '#FFFFFF', 'S', null), (141, 12, 'Trắng', '#FFFFFF', 'M', null), (142, 12, 'Trắng', '#FFFFFF', 'L', null),
(143, 12, 'Xám', '#6B7280', 'S', null), (144, 12, 'Xám', '#6B7280', 'M', null), (145, 12, 'Xám', '#6B7280', 'L', null),

-- Giày Sneaker Nam (Product 13) - 3 colors x 5 sizes = 15 variants
(146, 13, 'Trắng', '#FFFFFF', '39', null), (147, 13, 'Trắng', '#FFFFFF', '40', null), (148, 13, 'Trắng', '#FFFFFF', '41', null), (149, 13, 'Trắng', '#FFFFFF', '42', null), (150, 13, 'Trắng', '#FFFFFF', '43', null),
(151, 13, 'Đen', '#000000', '39', null), (152, 13, 'Đen', '#000000', '40', null), (153, 13, 'Đen', '#000000', '41', null), (154, 13, 'Đen', '#000000', '42', null), (155, 13, 'Đen', '#000000', '43', null),
(156, 13, 'Xám', '#6B7280', '39', null), (157, 13, 'Xám', '#6B7280', '40', null), (158, 13, 'Xám', '#6B7280', '41', null), (159, 13, 'Xám', '#6B7280', '42', null), (160, 13, 'Xám', '#6B7280', '43', null),

-- Giày Boot Nữ (Product 14) - 2 colors x 4 sizes = 8 variants
(161, 14, 'Đen', '#000000', '36', null), (162, 14, 'Đen', '#000000', '37', null), (163, 14, 'Đen', '#000000', '38', null), (164, 14, 'Đen', '#000000', '39', null),
(165, 14, 'Nâu', '#92400E', '36', null), (166, 14, 'Nâu', '#92400E', '37', null), (167, 14, 'Nâu', '#92400E', '38', null), (168, 14, 'Nâu', '#92400E', '39', null),

-- Sandal Nữ (Product 15) - 3 colors x 4 sizes = 12 variants
(169, 15, 'Đen', '#000000', '36', null), (170, 15, 'Đen', '#000000', '37', null), (171, 15, 'Đen', '#000000', '38', null), (172, 15, 'Đen', '#000000', '39', null),
(173, 15, 'Nâu', '#92400E', '36', null), (174, 15, 'Nâu', '#92400E', '37', null), (175, 15, 'Nâu', '#92400E', '38', null), (176, 15, 'Nâu', '#92400E', '39', null),
(177, 15, 'Be', '#D97706', '36', null), (178, 15, 'Be', '#D97706', '37', null), (179, 15, 'Be', '#D97706', '38', null), (180, 15, 'Be', '#D97706', '39', null),

-- Túi Tote (Product 16) - 3 colors x 1 size = 3 variants
(181, 16, 'Be', '#D97706', 'Free Size', null),
(182, 16, 'Đen', '#000000', 'Free Size', null),
(183, 16, 'Navy', '#1E3A8A', 'Free Size', null),

-- Nón Snapback (Product 17) - 3 colors x 1 size = 3 variants
(184, 17, 'Đen', '#000000', 'Free Size', null),
(185, 17, 'Xám', '#6B7280', 'Free Size', null),
(186, 17, 'Navy', '#1E3A8A', 'Free Size', null),

-- Thắt Lưng (Product 18) - 2 colors x 3 sizes = 6 variants
(187, 18, 'Đen', '#000000', 'S', null), (188, 18, 'Đen', '#000000', 'M', null), (189, 18, 'Đen', '#000000', 'L', null),
(190, 18, 'Nâu', '#92400E', 'S', null), (191, 18, 'Nâu', '#92400E', 'M', null), (192, 18, 'Nâu', '#92400E', 'L', null);

-- Insert product_info for new products (19-50)
INSERT IGNORE INTO product_info (id, product_id, color_name, color_hex_code, size_name, image) VALUES
-- Áo Polo Nam (19) - 3 colors x 4 sizes = 12 variants
(193, 19, 'Trắng', '#FFFFFF', 'S', null), (194, 19, 'Trắng', '#FFFFFF', 'M', null), (195, 19, 'Trắng', '#FFFFFF', 'L', null), (196, 19, 'Trắng', '#FFFFFF', 'XL', null),
(197, 19, 'Xanh Navy', '#1E3A8A', 'S', null), (198, 19, 'Xanh Navy', '#1E3A8A', 'M', null), (199, 19, 'Xanh Navy', '#1E3A8A', 'L', null), (200, 19, 'Xanh Navy', '#1E3A8A', 'XL', null),
(201, 19, 'Đỏ', '#DC2626', 'S', null), (202, 19, 'Đỏ', '#DC2626', 'M', null), (203, 19, 'Đỏ', '#DC2626', 'L', null), (204, 19, 'Đỏ', '#DC2626', 'XL', null),

-- Áo Len Nam (20) - 3 colors x 4 sizes = 12 variants
(205, 20, 'Xám', '#6B7280', 'S', null), (206, 20, 'Xám', '#6B7280', 'M', null), (207, 20, 'Xám', '#6B7280', 'L', null), (208, 20, 'Xám', '#6B7280', 'XL', null),
(209, 20, 'Đen', '#000000', 'S', null), (210, 20, 'Đen', '#000000', 'M', null), (211, 20, 'Đen', '#000000', 'L', null), (212, 20, 'Đen', '#000000', 'XL', null),
(213, 20, 'Navy', '#1E3A8A', 'S', null), (214, 20, 'Navy', '#1E3A8A', 'M', null), (215, 20, 'Navy', '#1E3A8A', 'L', null), (216, 20, 'Navy', '#1E3A8A', 'XL', null),

-- Áo Khoác Nam (21) - 3 colors x 4 sizes = 12 variants
(217, 21, 'Đen', '#000000', 'S', null), (218, 21, 'Đen', '#000000', 'M', null), (219, 21, 'Đen', '#000000', 'L', null), (220, 21, 'Đen', '#000000', 'XL', null),
(221, 21, 'Xanh', '#2563EB', 'S', null), (222, 21, 'Xanh', '#2563EB', 'M', null), (223, 21, 'Xanh', '#2563EB', 'L', null), (224, 21, 'Xanh', '#2563EB', 'XL', null),
(225, 21, 'Xám', '#6B7280', 'S', null), (226, 21, 'Xám', '#6B7280', 'M', null), (227, 21, 'Xám', '#6B7280', 'L', null), (228, 21, 'Xám', '#6B7280', 'XL', null),

-- Áo Vest Nam (22) - 2 colors x 4 sizes = 8 variants
(229, 22, 'Đen', '#000000', 'S', null), (230, 22, 'Đen', '#000000', 'M', null), (231, 22, 'Đen', '#000000', 'L', null), (232, 22, 'Đen', '#000000', 'XL', null),
(233, 22, 'Xám', '#6B7280', 'S', null), (234, 22, 'Xám', '#6B7280', 'M', null), (235, 22, 'Xám', '#6B7280', 'L', null), (236, 22, 'Xám', '#6B7280', 'XL', null),

-- Áo Thun Graphic (23) - 3 colors x 4 sizes = 12 variants
(237, 23, 'Trắng', '#FFFFFF', 'S', null), (238, 23, 'Trắng', '#FFFFFF', 'M', null), (239, 23, 'Trắng', '#FFFFFF', 'L', null), (240, 23, 'Trắng', '#FFFFFF', 'XL', null),
(241, 23, 'Đen', '#000000', 'S', null), (242, 23, 'Đen', '#000000', 'M', null), (243, 23, 'Đen', '#000000', 'L', null), (244, 23, 'Đen', '#000000', 'XL', null),
(245, 23, 'Xám', '#6B7280', 'S', null), (246, 23, 'Xám', '#6B7280', 'M', null), (247, 23, 'Xám', '#6B7280', 'L', null), (248, 23, 'Xám', '#6B7280', 'XL', null),

-- Áo Cardigan Nam (24) - 3 colors x 4 sizes = 12 variants
(249, 24, 'Be', '#D97706', 'S', null), (250, 24, 'Be', '#D97706', 'M', null), (251, 24, 'Be', '#D97706', 'L', null), (252, 24, 'Be', '#D97706', 'XL', null),
(253, 24, 'Xanh', '#2563EB', 'S', null), (254, 24, 'Xanh', '#2563EB', 'M', null), (255, 24, 'Xanh', '#2563EB', 'L', null), (256, 24, 'Xanh', '#2563EB', 'XL', null),
(257, 24, 'Đen', '#000000', 'S', null), (258, 24, 'Đen', '#000000', 'M', null), (259, 24, 'Đen', '#000000', 'L', null), (260, 24, 'Đen', '#000000', 'XL', null),

-- Áo Tank Top Nam (25) - 3 colors x 4 sizes = 12 variants
(261, 25, 'Trắng', '#FFFFFF', 'S', null), (262, 25, 'Trắng', '#FFFFFF', 'M', null), (263, 25, 'Trắng', '#FFFFFF', 'L', null), (264, 25, 'Trắng', '#FFFFFF', 'XL', null),
(265, 25, 'Đen', '#000000', 'S', null), (266, 25, 'Đen', '#000000', 'M', null), (267, 25, 'Đen', '#000000', 'L', null), (268, 25, 'Đen', '#000000', 'XL', null),
(269, 25, 'Xanh', '#2563EB', 'S', null), (270, 25, 'Xanh', '#2563EB', 'M', null), (271, 25, 'Xanh', '#2563EB', 'L', null), (272, 25, 'Xanh', '#2563EB', 'XL', null),

-- Áo Cardigan Nữ (26) - 3 colors x 3 sizes = 9 variants
(273, 26, 'Trắng', '#FFFFFF', 'S', null), (274, 26, 'Trắng', '#FFFFFF', 'M', null), (275, 26, 'Trắng', '#FFFFFF', 'L', null),
(276, 26, 'Hồng', '#EC4899', 'S', null), (277, 26, 'Hồng', '#EC4899', 'M', null), (278, 26, 'Hồng', '#EC4899', 'L', null),
(279, 26, 'Xanh', '#2563EB', 'S', null), (280, 26, 'Xanh', '#2563EB', 'M', null), (281, 26, 'Xanh', '#2563EB', 'L', null),

-- Áo Trench Coat (27) - 2 colors x 3 sizes = 6 variants
(282, 27, 'Be', '#D97706', 'S', null), (283, 27, 'Be', '#D97706', 'M', null), (284, 27, 'Be', '#D97706', 'L', null),
(285, 27, 'Đen', '#000000', 'S', null), (286, 27, 'Đen', '#000000', 'M', null), (287, 27, 'Đen', '#000000', 'L', null),

-- Áo Thun Oversize (28) - 3 colors x 3 sizes = 9 variants
(288, 28, 'Trắng', '#FFFFFF', 'S', null), (289, 28, 'Trắng', '#FFFFFF', 'M', null), (290, 28, 'Trắng', '#FFFFFF', 'L', null),
(291, 28, 'Đen', '#000000', 'S', null), (292, 28, 'Đen', '#000000', 'M', null), (293, 28, 'Đen', '#000000', 'L', null),
(294, 28, 'Xám', '#6B7280', 'S', null), (295, 28, 'Xám', '#6B7280', 'M', null), (296, 28, 'Xám', '#6B7280', 'L', null),

-- Áo Blazer Slim (29) - 2 colors x 3 sizes = 6 variants
(297, 29, 'Đen', '#000000', 'S', null), (298, 29, 'Đen', '#000000', 'M', null), (299, 29, 'Đen', '#000000', 'L', null),
(300, 29, 'Navy', '#1E3A8A', 'S', null), (301, 29, 'Navy', '#1E3A8A', 'M', null), (302, 29, 'Navy', '#1E3A8A', 'L', null),

-- Áo Sweater Nữ (30) - 3 colors x 3 sizes = 9 variants
(303, 30, 'Trắng', '#FFFFFF', 'S', null), (304, 30, 'Trắng', '#FFFFFF', 'M', null), (305, 30, 'Trắng', '#FFFFFF', 'L', null),
(306, 30, 'Xám', '#6B7280', 'S', null), (307, 30, 'Xám', '#6B7280', 'M', null), (308, 30, 'Xám', '#6B7280', 'L', null),
(309, 30, 'Hồng', '#EC4899', 'S', null), (310, 30, 'Hồng', '#EC4899', 'M', null), (311, 30, 'Hồng', '#EC4899', 'L', null),

-- Áo Vest Nữ (31) - 2 colors x 3 sizes = 6 variants
(312, 31, 'Đen', '#000000', 'S', null), (313, 31, 'Đen', '#000000', 'M', null), (314, 31, 'Đen', '#000000', 'L', null),
(315, 31, 'Xám', '#6B7280', 'S', null), (316, 31, 'Xám', '#6B7280', 'M', null), (317, 31, 'Xám', '#6B7280', 'L', null),

-- Áo Tank Top Nữ (32) - 3 colors x 3 sizes = 9 variants
(318, 32, 'Trắng', '#FFFFFF', 'S', null), (319, 32, 'Trắng', '#FFFFFF', 'M', null), (320, 32, 'Trắng', '#FFFFFF', 'L', null),
(321, 32, 'Đen', '#000000', 'S', null), (322, 32, 'Đen', '#000000', 'M', null), (323, 32, 'Đen', '#000000', 'L', null),
(324, 32, 'Hồng', '#EC4899', 'S', null), (325, 32, 'Hồng', '#EC4899', 'M', null), (326, 32, 'Hồng', '#EC4899', 'L', null),

-- Quần Jogger Nam (33) - 3 colors x 5 sizes = 15 variants
(327, 33, 'Đen', '#000000', '28', null), (328, 33, 'Đen', '#000000', '29', null), (329, 33, 'Đen', '#000000', '30', null), (330, 33, 'Đen', '#000000', '32', null), (331, 33, 'Đen', '#000000', '34', null),
(332, 33, 'Xám', '#6B7280', '28', null), (333, 33, 'Xám', '#6B7280', '29', null), (334, 33, 'Xám', '#6B7280', '30', null), (335, 33, 'Xám', '#6B7280', '32', null), (336, 33, 'Xám', '#6B7280', '34', null),
(337, 33, 'Navy', '#1E3A8A', '28', null), (338, 33, 'Navy', '#1E3A8A', '29', null), (339, 33, 'Navy', '#1E3A8A', '30', null), (340, 33, 'Navy', '#1E3A8A', '32', null), (341, 33, 'Navy', '#1E3A8A', '34', null),

-- Quần Cargo Nam (34) - 2 colors x 5 sizes = 10 variants
(342, 34, 'Xanh', '#2563EB', '28', null), (343, 34, 'Xanh', '#2563EB', '29', null), (344, 34, 'Xanh', '#2563EB', '30', null), (345, 34, 'Xanh', '#2563EB', '32', null), (346, 34, 'Xanh', '#2563EB', '34', null),
(347, 34, 'Đen', '#000000', '28', null), (348, 34, 'Đen', '#000000', '29', null), (349, 34, 'Đen', '#000000', '30', null), (350, 34, 'Đen', '#000000', '32', null), (351, 34, 'Đen', '#000000', '34', null),

-- Quần Tây Nam (35) - 2 colors x 5 sizes = 10 variants
(352, 35, 'Đen', '#000000', '28', null), (353, 35, 'Đen', '#000000', '29', null), (354, 35, 'Đen', '#000000', '30', null), (355, 35, 'Đen', '#000000', '32', null), (356, 35, 'Đen', '#000000', '34', null),
(357, 35, 'Xám', '#6B7280', '28', null), (358, 35, 'Xám', '#6B7280', '29', null), (359, 35, 'Xám', '#6B7280', '30', null), (360, 35, 'Xám', '#6B7280', '32', null), (361, 35, 'Xám', '#6B7280', '34', null),

-- Quần Ống Rộng Nam (36) - 3 colors x 4 sizes = 12 variants
(362, 36, 'Trắng', '#FFFFFF', '28', null), (363, 36, 'Trắng', '#FFFFFF', '29', null), (364, 36, 'Trắng', '#FFFFFF', '30', null), (365, 36, 'Trắng', '#FFFFFF', '32', null),
(366, 36, 'Be', '#D97706', '28', null), (367, 36, 'Be', '#D97706', '29', null), (368, 36, 'Be', '#D97706', '30', null), (369, 36, 'Be', '#D97706', '32', null),
(370, 36, 'Xanh', '#2563EB', '28', null), (371, 36, 'Xanh', '#2563EB', '29', null), (372, 36, 'Xanh', '#2563EB', '30', null), (373, 36, 'Xanh', '#2563EB', '32', null),

-- Quần Legging Nữ (37) - 3 colors x 4 sizes = 12 variants
(374, 37, 'Đen', '#000000', 'S', null), (375, 37, 'Đen', '#000000', 'M', null), (376, 37, 'Đen', '#000000', 'L', null), (377, 37, 'Đen', '#000000', 'XL', null),
(378, 37, 'Xám', '#6B7280', 'S', null), (379, 37, 'Xám', '#6B7280', 'M', null), (380, 37, 'Xám', '#6B7280', 'L', null), (381, 37, 'Xám', '#6B7280', 'XL', null),
(382, 37, 'Hồng', '#EC4899', 'S', null), (383, 37, 'Hồng', '#EC4899', 'M', null), (384, 37, 'Hồng', '#EC4899', 'L', null), (385, 37, 'Hồng', '#EC4899', 'XL', null),

-- Quần Tây Nữ (38) - 2 colors x 5 sizes = 10 variants
(386, 38, 'Đen', '#000000', '25', null), (387, 38, 'Đen', '#000000', '26', null), (388, 38, 'Đen', '#000000', '27', null), (389, 38, 'Đen', '#000000', '28', null), (390, 38, 'Đen', '#000000', '29', null),
(391, 38, 'Xanh', '#2563EB', '25', null), (392, 38, 'Xanh', '#2563EB', '26', null), (393, 38, 'Xanh', '#2563EB', '27', null), (394, 38, 'Xanh', '#2563EB', '28', null), (395, 38, 'Xanh', '#2563EB', '29', null),

-- Quần Short Nữ (39) - 3 colors x 4 sizes = 12 variants
(396, 39, 'Xanh', '#2563EB', '25', null), (397, 39, 'Xanh', '#2563EB', '26', null), (398, 39, 'Xanh', '#2563EB', '27', null), (399, 39, 'Xanh', '#2563EB', '28', null),
(400, 39, 'Đen', '#000000', '25', null), (401, 39, 'Đen', '#000000', '26', null), (402, 39, 'Đen', '#000000', '27', null), (403, 39, 'Đen', '#000000', '28', null),
(404, 39, 'Trắng', '#FFFFFF', '25', null), (405, 39, 'Trắng', '#FFFFFF', '26', null), (406, 39, 'Trắng', '#FFFFFF', '27', null), (407, 39, 'Trắng', '#FFFFFF', '28', null),

-- Quần Jogger Nữ (40) - 3 colors x 4 sizes = 12 variants
(408, 40, 'Đen', '#000000', 'S', null), (409, 40, 'Đen', '#000000', 'M', null), (410, 40, 'Đen', '#000000', 'L', null), (411, 40, 'Đen', '#000000', 'XL', null),
(412, 40, 'Xám', '#6B7280', 'S', null), (413, 40, 'Xám', '#6B7280', 'M', null), (414, 40, 'Xám', '#6B7280', 'L', null), (415, 40, 'Xám', '#6B7280', 'XL', null),
(416, 40, 'Hồng', '#EC4899', 'S', null), (417, 40, 'Hồng', '#EC4899', 'M', null), (418, 40, 'Hồng', '#EC4899', 'L', null), (419, 40, 'Hồng', '#EC4899', 'XL', null),

-- Giày Loafers (41) - 2 colors x 5 sizes = 10 variants
(420, 41, 'Nâu', '#92400E', '39', null), (421, 41, 'Nâu', '#92400E', '40', null), (422, 41, 'Nâu', '#92400E', '41', null), (423, 41, 'Nâu', '#92400E', '42', null), (424, 41, 'Nâu', '#92400E', '43', null),
(425, 41, 'Đen', '#000000', '39', null), (426, 41, 'Đen', '#000000', '40', null), (427, 41, 'Đen', '#000000', '41', null), (428, 41, 'Đen', '#000000', '42', null), (429, 41, 'Đen', '#000000', '43', null),

-- Giày Pumps (42) - 2 colors x 4 sizes = 8 variants
(430, 42, 'Đen', '#000000', '36', null), (431, 42, 'Đen', '#000000', '37', null), (432, 42, 'Đen', '#000000', '38', null), (433, 42, 'Đen', '#000000', '39', null),
(434, 42, 'Nâu', '#92400E', '36', null), (435, 42, 'Nâu', '#92400E', '37', null), (436, 42, 'Nâu', '#92400E', '38', null), (437, 42, 'Nâu', '#92400E', '39', null),

-- Giày Thể Thao (43) - 3 colors x 5 sizes = 15 variants
(438, 43, 'Trắng', '#FFFFFF', '39', null), (439, 43, 'Trắng', '#FFFFFF', '40', null), (440, 43, 'Trắng', '#FFFFFF', '41', null), (441, 43, 'Trắng', '#FFFFFF', '42', null), (442, 43, 'Trắng', '#FFFFFF', '43', null),
(443, 43, 'Đen', '#000000', '39', null), (444, 43, 'Đen', '#000000', '40', null), (445, 43, 'Đen', '#000000', '41', null), (446, 43, 'Đen', '#000000', '42', null), (447, 43, 'Đen', '#000000', '43', null),
(448, 43, 'Xanh', '#2563EB', '39', null), (449, 43, 'Xanh', '#2563EB', '40', null), (450, 43, 'Xanh', '#2563EB', '41', null), (451, 43, 'Xanh', '#2563EB', '42', null), (452, 43, 'Xanh', '#2563EB', '43', null),

-- Sandal Flip Flops (44) - 3 colors x 4 sizes = 12 variants
(453, 44, 'Đen', '#000000', '39', null), (454, 44, 'Đen', '#000000', '40', null), (455, 44, 'Đen', '#000000', '41', null), (456, 44, 'Đen', '#000000', '42', null),
(457, 44, 'Xanh', '#2563EB', '39', null), (458, 44, 'Xanh', '#2563EB', '40', null), (459, 44, 'Xanh', '#2563EB', '41', null), (460, 44, 'Xanh', '#2563EB', '42', null),
(461, 44, 'Đỏ', '#DC2626', '39', null), (462, 44, 'Đỏ', '#DC2626', '40', null), (463, 44, 'Đỏ', '#DC2626', '41', null), (464, 44, 'Đỏ', '#DC2626', '42', null),

-- Túi Tote Nữ (45) - 3 colors x 1 size = 3 variants
(465, 45, 'Đen', '#000000', 'Free Size', null),
(466, 45, 'Be', '#D97706', 'Free Size', null),
(467, 45, 'Hồng', '#EC4899', 'Free Size', null),

-- Ví Nam (46) - 2 colors x 1 size = 2 variants
(468, 46, 'Nâu', '#92400E', 'Free Size', null),
(469, 46, 'Đen', '#000000', 'Free Size', null),

-- Kính Mát (47) - 3 colors x 1 size = 3 variants
(470, 47, 'Đen', '#000000', 'Free Size', null),
(471, 47, 'Xanh', '#2563EB', 'Free Size', null),
(472, 47, 'Hồng', '#EC4899', 'Free Size', null),

-- Khăn Scarf (48) - 3 colors x 1 size = 3 variants
(473, 48, 'Đỏ', '#DC2626', 'Free Size', null),
(474, 48, 'Xanh', '#2563EB', 'Free Size', null),
(475, 48, 'Vàng', '#F59E0B', 'Free Size', null),

-- Vòng Tay (49) - 2 colors x 1 size = 2 variants
(476, 49, 'Đen', '#000000', 'Free Size', null),
(477, 49, 'Nâu', '#92400E', 'Free Size', null),

-- Mũ Lưỡi Trai (50) - 3 colors x 1 size = 3 variants
(478, 50, 'Đen', '#000000', 'Free Size', null),
(479, 50, 'Trắng', '#FFFFFF', 'Free Size', null),
(480, 50, 'Navy', '#1E3A8A', 'Free Size', null);

-- Insert stock_items (tồn kho cho từng sản phẩm ở 3 kho)
-- Format: (id, stock_id, product_info_id, quantity)
-- Kho 1 (Hà Nội) - stock_id = 1
INSERT IGNORE INTO stock_items (id, stock_id, product_info_id, quantity) VALUES
-- Áo Thun Nam Basic (1)
(1, 1, 1, 80), (2, 1, 2, 120), (3, 1, 3, 60),
-- Áo Sơ Mi Nam (2)
(4, 1, 2, 45), (5, 2, 2, 70), (6, 3, 2, 35),
-- Hoodie Nam (3)
(7, 1, 3, 30), (8, 2, 3, 50), (9, 3, 3, 25),
-- Áo Thun Nữ (4)
(10, 1, 4, 90), (11, 2, 4, 110), (12, 3, 4, 70),
-- Blouse Nữ (5)
(13, 1, 5, 40), (14, 2, 5, 60), (15, 3, 5, 30),
-- Blazer Nữ (6)
(16, 1, 6, 25), (17, 2, 6, 35), (18, 3, 6, 20),
-- Jeans Nam (7)
(19, 1, 7, 60), (20, 2, 7, 85), (21, 3, 7, 45),
-- Kaki Nam (8)
(22, 1, 8, 50), (23, 2, 8, 70), (24, 3, 8, 40),
-- Short Nam (9)
(25, 1, 9, 75), (26, 2, 9, 95), (27, 3, 9, 55),
-- Jeans Nữ (10)
(28, 1, 10, 55), (29, 2, 10, 80), (30, 3, 10, 40),
-- Chân Váy (11)
(31, 1, 11, 65), (32, 2, 11, 85), (33, 3, 11, 50),
-- Culottes (12)
(34, 1, 12, 45), (35, 2, 12, 65), (36, 3, 12, 35),
-- Sneaker Nam (13)
(37, 1, 13, 25), (38, 2, 13, 40), (39, 3, 13, 20),
-- Boot Nữ (14)
(40, 1, 14, 20), (41, 2, 14, 35), (42, 3, 14, 15),
-- Sandal Nữ (15)
(43, 1, 15, 35), (44, 2, 15, 50), (45, 3, 15, 25),
-- Túi Tote (16)
(46, 1, 16, 40), (47, 2, 16, 60), (48, 3, 16, 30),
-- Nón Snapback (17)
(49, 1, 17, 55), (50, 2, 17, 75), (51, 3, 17, 45),
-- Thắt Lưng (18)
(52, 1, 18, 30), (53, 2, 18, 45), (54, 3, 18, 25);

-- Insert discounts (mã giảm giá)
-- Insert discounts (matching entity fields: name, title, description, discountAmount, discountPercent, type, category, usageCount, maxUsage, startDate, endDate, isActive)
INSERT IGNORE INTO discounts (id, name, title, description, discount_amount, discount_percent, type, category, usage_count, max_usage, start_date, end_date, is_active) VALUES
(1, 'WELCOME10', 'Ưu đãi khách mới', 'Giảm 10% cho khách hàng mới', 0, 10.0, 'PERCENT', 'PRODUCT', 0, 1000, '2025-01-01', '2025-12-31', true),
(2, 'SALE50K', 'Giảm 50K', 'Giảm 50k cho đơn hàng từ 500k', 50000, 0.0, 'FIXED', 'PRODUCT', 0, 500, '2025-01-01', '2025-12-31', true),
(3, 'SUMMER20', 'Khuyến mãi hè', 'Khuyến mãi hè - Giảm 20%', 0, 20.0, 'PERCENT', 'PRODUCT', 0, 200, '2025-06-01', '2025-08-31', true),
(4, 'FREESHIP', 'Miễn phí ship', 'Miễn phí vận chuyển', 30000, 0.0, 'FIXED', 'SHIPPING', 0, 2000, '2025-01-01', '2025-12-31', true),
(5, 'VIP15', 'Ưu đãi VIP', 'Ưu đãi VIP - Giảm 15%', 0, 15.0, 'PERCENT', 'PRODUCT', 0, 100, '2025-01-01', '2025-12-31', true),
(6, 'FLASH30', 'Flash Sale 30%', 'Giảm 30% cho sản phẩm flash sale', 0, 30.0, 'PERCENT', 'PRODUCT', 0, 300, '2025-01-01', '2025-12-31', true),
(7, 'SHIP20K', 'Giảm ship 20K', 'Giảm 20k phí vận chuyển', 20000, 0.0, 'FIXED', 'SHIPPING', 0, 500, '2025-01-01', '2025-12-31', true),
(8, 'NEWYEAR25', 'Tết Nguyên Đán', 'Giảm 25% mừng năm mới', 0, 25.0, 'PERCENT', 'PRODUCT', 0, 200, '2025-02-01', '2025-02-15', true),

-- Mã Free Ship (3 mã)
(9, 'SHIPFREE99', 'Freeship 99K', 'Miễn phí vận chuyển cho đơn từ 99K', 35000, 0.0, 'FIXED', 'SHIPPING', 0, 1500, '2025-01-01', '2025-12-31', true),
(10, 'SHIPVIP', 'Freeship VIP', 'Miễn phí ship cho khách hàng VIP', 50000, 0.0, 'FIXED', 'SHIPPING', 0, 800, '2025-01-01', '2025-12-31', true),
(11, 'SHIP0DONG', 'Ship 0 đồng', 'Miễn phí hoàn toàn phí vận chuyển', 40000, 0.0, 'FIXED', 'SHIPPING', 0, 1000, '2025-01-01', '2025-06-30', true),

-- Mã giảm giá theo sản phẩm - Giảm theo tiền (5 mã)
(12, 'GIAM30K', 'Giảm 30K', 'Giảm 30K cho đơn hàng từ 300K', 30000, 0.0, 'FIXED', 'PRODUCT', 0, 600, '2025-01-01', '2025-12-31', true),
(13, 'SALE100K', 'Giảm 100K', 'Giảm 100K cho đơn hàng từ 1 triệu', 100000, 0.0, 'FIXED', 'PRODUCT', 0, 400, '2025-01-01', '2025-12-31', true),
(14, 'TIETKIEMHON', 'Tiết kiệm hơn', 'Giảm ngay 70K cho đơn từ 700K', 70000, 0.0, 'FIXED', 'PRODUCT', 0, 550, '2025-01-01', '2025-12-31', true),
(15, 'MEGA200K', 'Mega Sale 200K', 'Giảm 200K cho đơn hàng từ 2 triệu', 200000, 0.0, 'FIXED', 'PRODUCT', 0, 250, '2025-01-01', '2025-12-31', true),
(16, 'KHACHHANG150K', 'Ưu đãi 150K', 'Giảm 150K cho khách hàng thân thiết', 150000, 0.0, 'FIXED', 'PRODUCT', 0, 350, '2025-01-01', '2025-12-31', true),

-- Mã giảm giá theo sản phẩm - Giảm theo % (5 mã)
(17, 'DONGXUAN12', 'Đón xuân 12%', 'Giảm 12% cho tất cả sản phẩm', 0, 12.0, 'PERCENT', 'PRODUCT', 0, 700, '2025-01-01', '2025-03-31', true),
(18, 'SIEUSALE35', 'Siêu sale 35%', 'Giảm 35% cho đơn hàng từ 800K', 0, 35.0, 'PERCENT', 'PRODUCT', 0, 450, '2025-01-01', '2025-12-31', true),
(19, 'KHUYENMAI18', 'Khuyến mại 18%', 'Giảm 18% cho khách hàng mới', 0, 18.0, 'PERCENT', 'PRODUCT', 0, 850, '2025-01-01', '2025-12-31', true),
(20, 'HOTDEAL40', 'Hot Deal 40%', 'Giảm 40% cho sản phẩm hot', 0, 40.0, 'PERCENT', 'PRODUCT', 0, 300, '2025-01-01', '2025-06-30', true),
(21, 'MEMBER22', 'Member 22%', 'Giảm 22% cho thành viên', 0, 22.0, 'PERCENT', 'PRODUCT', 0, 650, '2025-01-01', '2025-12-31', true),

-- Mã giảm giá đặc biệt theo dịp (5 mã)
(22, 'QUOCTHANH50', 'Quốc khánh 50%', 'Giảm 50% nhân dịp Quốc Khánh 2/9', 0, 50.0, 'PERCENT', 'PRODUCT', 0, 500, '2025-08-25', '2025-09-05', true),
(23, 'PHUNUTET', 'Phụ nữ 8/3', 'Giảm 80K nhân ngày Phụ nữ 8/3', 80000, 0.0, 'FIXED', 'PRODUCT', 0, 600, '2025-03-01', '2025-03-10', true),
(24, 'HALLOWEEN28', 'Halloween 28%', 'Giảm 28% nhân dịp Halloween', 0, 28.0, 'PERCENT', 'PRODUCT', 0, 400, '2025-10-25', '2025-11-01', true),
(25, 'NOEL45', 'Giáng sinh 45%', 'Giảm 45% mừng Giáng sinh', 0, 45.0, 'PERCENT', 'PRODUCT', 0, 550, '2025-12-20', '2025-12-26', true),
(26, 'BLACKFRIDAY', 'Black Friday', 'Giảm 300K cho Black Friday', 300000, 0.0, 'FIXED', 'PRODUCT', 0, 350, '2025-11-28', '2025-11-30', true);

-- Insert product_sales (Giảm giá cho từng sản phẩm cụ thể)
-- Cấu trúc: id, product_id, original_price, discount_percent, discounted_price, start_date, end_date
INSERT IGNORE INTO product_sales (id, product_id, original_price, discount_percent, discounted_price, start_date, end_date) VALUES

-- ===== GIẢM GIÁ CÓ THỜI HẠN (khoảng 1-3 tháng) =====

-- Flash Sale Tết 2025 (Từ 01/01 - 28/02/2025) - 2 tháng
(1, 1, 179000, 30, 125300, '2025-01-01 00:00:00', '2025-02-28 23:59:59'),  -- Áo Thun Nam Basic: 179K -> 125K (-30%)
(2, 3, 499000, 35, 324350, '2025-01-01 00:00:00', '2025-02-28 23:59:59'),  -- Áo Hoodie Nam: 499K -> 324K (-35%)
(3, 13, 799000, 25, 599250, '2025-01-01 00:00:00', '2025-02-28 23:59:59'), -- Giày Sneaker Nam: 799K -> 599K (-25%)

-- Khuyến Mãi Phụ Nữ 8/3 (Từ 01/03 - 15/03/2025) - 15 ngày
(4, 4, 199000, 40, 119400, '2025-03-01 00:00:00', '2025-03-15 23:59:59'),  -- Áo Crop Top Nữ: 199K -> 119K (-40%)
(5, 5, 459000, 35, 298350, '2025-03-01 00:00:00', '2025-03-15 23:59:59'),  -- Áo Blouse Nữ: 459K -> 298K (-35%)
(6, 6, 649000, 30, 454300, '2025-03-01 00:00:00', '2025-03-15 23:59:59'),  -- Blazer Nữ: 649K -> 454K (-30%)
(7, 14, 899000, 25, 674250, '2025-03-01 00:00:00', '2025-03-15 23:59:59'), -- Boot Nữ Chelsea: 899K -> 674K (-25%)

-- Sale Mùa Hè (Từ 01/05 - 31/07/2025) - 3 tháng
(8, 9, 259000, 40, 155400, '2025-05-01 00:00:00', '2025-07-31 23:59:59'),  -- Short Nam: 259K -> 155K (-40%)
(9, 15, 459000, 35, 298350, '2025-05-01 00:00:00', '2025-07-31 23:59:59'), -- Sandal Nữ: 459K -> 298K (-35%)
(10, 28, 259000, 30, 181300, '2025-05-01 00:00:00', '2025-07-31 23:59:59'), -- Áo Thun Nữ Oversize: 259K -> 181K (-30%)
(11, 32, 199000, 25, 149250, '2025-05-01 00:00:00', '2025-07-31 23:59:59'), -- Tank Top Nữ: 199K -> 149K (-25%)

-- Back to School (Từ 15/08 - 30/09/2025) - 1.5 tháng
(12, 16, 259000, 30, 181300, '2025-08-15 00:00:00', '2025-09-30 23:59:59'), -- Túi Tote Canvas: 259K -> 181K (-30%)
(13, 17, 219000, 25, 164250, '2025-08-15 00:00:00', '2025-09-30 23:59:59'), -- Nón Snapback: 219K -> 164K (-25%)
(14, 43, 599000, 20, 479200, '2025-08-15 00:00:00', '2025-09-30 23:59:59'), -- Giày Thể Thao Unisex: 599K -> 479K (-20%)

-- Singles Day 11/11 (Từ 01/11 - 15/11/2025) - 15 ngày
(15, 2, 399000, 45, 219450, '2025-11-01 00:00:00', '2025-11-15 23:59:59'), -- Áo Sơ Mi Nam: 399K -> 219K (-45%)
(16, 7, 549000, 40, 329400, '2025-11-01 00:00:00', '2025-11-15 23:59:59'), -- Jeans Nam: 549K -> 329K (-40%)
(17, 10, 599000, 40, 359400, '2025-11-01 00:00:00', '2025-11-15 23:59:59'), -- Jeans Nữ: 599K -> 359K (-40%)

-- Black Friday (Từ 20/11 - 30/11/2025) - 10 ngày
(18, 20, 549000, 50, 274500, '2025-11-20 00:00:00', '2025-11-30 23:59:59'), -- Áo Len Nam: 549K -> 275K (-50%)
(19, 21, 799000, 45, 439450, '2025-11-20 00:00:00', '2025-11-30 23:59:59'), -- Jacket Nam: 799K -> 439K (-45%)
(20, 27, 969000, 45, 532950, '2025-11-20 00:00:00', '2025-11-30 23:59:59'), -- Trench Coat Nữ: 969K -> 533K (-45%)

-- Giáng Sinh (Từ 10/12/2025 - 31/12/2025) - 21 ngày
(21, 19, 319000, 35, 207350, '2025-12-10 00:00:00', '2025-12-31 23:59:59'), -- Polo Nam: 319K -> 207K (-35%)
(22, 26, 499000, 30, 349300, '2025-12-10 00:00:00', '2025-12-31 23:59:59'), -- Cardigan Nam: 499K -> 349K (-30%)
(23, 30, 459000, 30, 321300, '2025-12-10 00:00:00', '2025-12-31 23:59:59'), -- Sweater Nữ: 459K -> 321K (-30%)

-- ===== GIẢM GIÁ VÔ HẠN (Không có end_date hoặc end_date rất xa) =====

-- Sản phẩm giảm giá thường xuyên để thanh lý kho
(24, 8, 419000, 20, 335200, '2025-01-01 00:00:00', NULL),  -- Quần Kaki Nam: 419K -> 335K (-20%) - VÔ HẠN
(25, 11, 359000, 25, 269250, '2025-01-01 00:00:00', NULL), -- Chân Váy: 359K -> 269K (-25%) - VÔ HẠN
(26, 12, 479000, 20, 383200, '2025-01-01 00:00:00', NULL), -- Culottes Nữ: 479K -> 383K (-20%) - VÔ HẠN

-- Sản phẩm outlet - giảm giá dài hạn (3 năm)
(27, 18, 419000, 30, 293300, '2025-01-01 00:00:00', '2027-12-31 23:59:59'), -- Thắt Lưng Da: 419K -> 293K (-30%)
(28, 23, 239000, 25, 179250, '2025-01-01 00:00:00', '2027-12-31 23:59:59'), -- Áo Thun Graphic: 239K -> 179K (-25%)
(29, 25, 179000, 20, 143200, '2025-01-01 00:00:00', '2027-12-31 23:59:59'), -- Tank Top Nam: 179K -> 143K (-20%)

-- Sale cố định trong năm 2025-2026 (dài hạn 2 năm)
(30, 24, 599000, 25, 449250, '2025-01-01 00:00:00', '2026-12-31 23:59:59'), -- Cardigan Nam: 599K -> 449K (-25%)
(31, 33, 399000, 20, 319200, '2025-01-01 00:00:00', '2026-12-31 23:59:59'), -- Jogger Nam: 399K -> 319K (-20%)
(32, 34, 499000, 25, 374250, '2025-01-01 00:00:00', '2026-12-31 23:59:59'), -- Cargo Nam: 499K -> 374K (-25%)
(33, 37, 319000, 15, 271150, '2025-01-01 00:00:00', '2026-12-31 23:59:59'), -- Legging Nữ: 319K -> 271K (-15%)
(34, 39, 349000, 20, 279200, '2025-01-01 00:00:00', '2026-12-31 23:59:59'), -- Short Nữ: 349K -> 279K (-20%)

-- Giảm giá cả năm 2025 (đến hết năm)
(35, 41, 899000, 15, 764150, '2025-01-01 00:00:00', '2025-12-31 23:59:59'), -- Giày Lười Nam: 899K -> 764K (-15%)
(36, 42, 689000, 20, 551200, '2025-01-01 00:00:00', '2025-12-31 23:59:59'), -- Cao Gót Nữ: 689K -> 551K (-20%)
(37, 44, 179000, 15, 152150, '2025-01-01 00:00:00', '2025-12-31 23:59:59'), -- Sandal Nam: 179K -> 152K (-15%)
(38, 45, 359000, 25, 269250, '2025-01-01 00:00:00', '2025-12-31 23:59:59'), -- Túi Xách Nữ: 359K -> 269K (-25%)
(39, 46, 499000, 20, 399200, '2025-01-01 00:00:00', '2025-12-31 23:59:59'), -- Ví Nam Da: 499K -> 399K (-20%)
(40, 47, 399000, 15, 339150, '2025-01-01 00:00:00', '2025-12-31 23:59:59'), -- Kính Mát: 399K -> 339K (-15%)
(41, 48, 319000, 20, 255200, '2025-01-01 00:00:00', '2025-12-31 23:59:59'), -- Khăn Choàng: 319K -> 255K (-20%)
(42, 49, 219000, 25, 164250, '2025-01-01 00:00:00', '2025-12-31 23:59:59'), -- Vòng Tay: 219K -> 164K (-25%)
(43, 50, 259000, 20, 207200, '2025-01-01 00:00:00', '2025-12-31 23:59:59'); -- Mũ Lưỡi Trai: 259K -> 207K (-20%)

-- Reset AUTO_INCREMENT for all tables
ALTER TABLE addresses AUTO_INCREMENT = 4;
ALTER TABLE stocks AUTO_INCREMENT = 4;
ALTER TABLE product_types AUTO_INCREMENT = 7;
ALTER TABLE products AUTO_INCREMENT = 51;
ALTER TABLE product_info AUTO_INCREMENT = 481;
ALTER TABLE stock_items AUTO_INCREMENT = 1441;

-- Create chat_rooms table
CREATE TABLE IF NOT EXISTS chat_rooms (
                                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                          user_id BIGINT,
                                          session_id VARCHAR(255),
    name VARCHAR(255) NOT NULL
    );

-- Create chats table for customer support chat system
CREATE TABLE IF NOT EXISTS chats (
                                     id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                     is_read BOOLEAN DEFAULT FALSE,
                                     message TEXT NOT NULL,
                                     name VARCHAR(255) NOT NULL,
    sender BIGINT NOT NULL, -- 0 for admin, userId for user
    chat_room_id BIGINT NOT NULL,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (chat_room_id) REFERENCES chat_rooms(id)
    );



-- Insert sample comments với user data thực tế
INSERT IGNORE INTO comments (id, product_id, author_id, author_name, text, comment_at, rate) VALUES
-- Comments for Áo Thun Nam Basic Cotton (Product 1)
(1, 1, 2, 'Hien Nguyen', 'Áo chất vải cotton 100% rất mát và thoải mái. Form regular fit vừa vặn, mặc đi làm hoặc đi chơi đều được. Giá cả phải chăng, sẽ mua thêm màu khác!', '2025-11-15 10:30:00', 5),
(2, 1, 3, 'Loc Nguyen', 'Chất lượng tốt so với giá tiền. Áo màu đen rất đẹp, không bị ra màu sau vài lần giặt. Giao hàng nhanh chóng.', '2025-11-16 15:45:00', 5),
(3, 1, 4, 'Ly Nguyen', 'Mua cho bạn trai, anh ấy rất thích. Form áo đẹp, vải mềm mại. Đáng để mua!', '2025-11-17 09:20:00', 4),
(4, 1, 5, 'Thuy Nguyen', 'Áo basic dễ phối đồ, chất cotton mát mẻ. Có nhiều màu để lựa chọn. Recommended!', '2025-11-18 14:10:00', 5),

-- Comments for Áo Sơ Mi Nam Công Sở (Product 2)
(5, 2, 6, 'Thanh Nguyen', 'Áo sơ mi công sở rất lịch sự. Vải cotton pha ít nhăn, dễ ủi. Form slim fit tôn dáng, phù hợp đi làm văn phòng.', '2025-11-10 11:00:00', 5),
(6, 2, 7, 'Tai Tran', 'Chất vải mềm mại, thoáng mát. Đã mua 3 màu để thay đổi. Shop giao hàng nhanh, đóng gói cẩn thận.', '2025-11-11 16:30:00', 5),
(7, 2, 2, 'Hien Nguyen', 'Sơ mi đẹp, form chuẩn. Mặc đi phỏng vấn rất ổn. Giá hợp lý cho chất lượng này.', '2025-11-12 08:45:00', 4),

-- Comments for Áo Hoodie Nam Streetwear (Product 3)
(8, 3, 3, 'Loc Nguyen', 'Hoodie chất nỉ dày dặn, ấm áp mùa đông. Form oversize trendy, túi kangaroo tiện lợi. Very cool!', '2025-11-08 19:20:00', 5),
(9, 3, 4, 'Ly Nguyen', 'Áo hoodie đẹp lắm! Chất vải cotton fleece mềm mịn. Mặc rất thoải mái, phù hợp đi chơi cuối tuần.', '2025-11-09 13:15:00', 5),
(10, 3, 6, 'Thanh Nguyen', 'Quality tốt, form oversize đúng gu streetwear. Hood vừa vặn, không bị rộng quá. Sẽ order thêm!', '2025-11-13 10:30:00', 5),

-- Comments for Áo Thun Nữ Crop Top (Product 4)
(11, 4, 4, 'Ly Nguyen', 'Crop top xinh xắn, chất cotton co giãn tốt. Form fitted ôm dáng vừa vặn. Màu hồng rất đẹp!', '2025-11-14 16:00:00', 5),
(12, 4, 5, 'Thuy Nguyen', 'Áo cute lắm, mặc đi chơi với quần jean hoặc chân váy đều xinh. Chất vải mát, không bị nóng.', '2025-11-15 11:20:00', 5),
(13, 4, 2, 'Hien Nguyen', 'Form crop vừa phải, không bị ngắn quá. Chất vải tốt, giặt không bị giãn. Đáng mua!', '2025-11-16 14:50:00', 4),

-- Comments for Áo Blouse Nữ Công Sở (Product 5)
(14, 5, 4, 'Ly Nguyen', 'Blouse rất thanh lịch, phù hợp môi trường công sở. Chất lụa mềm mại, tay bồng nhẹ nhàng. Love it!', '2025-11-07 09:30:00', 5),
(15, 5, 5, 'Thuy Nguyen', 'Áo đẹp, chất vải tốt. Mặc đi làm rất sang trọng. Giá hơi cao nhưng chất lượng xứng đáng.', '2025-11-08 15:40:00', 4),

-- Comments for Áo Khoác Blazer Nữ (Product 6)
(16, 6, 7, 'Tai Tran', 'Blazer form fitted sang trọng. Chất vải polyester cao cấp, thiết kế 1 button hiện đại. Mặc đi họp hoặc tiệc rất ổn.', '2025-11-09 20:15:00', 5),
(17, 6, 4, 'Ly Nguyen', 'Áo khoác đẹp lắm! Form ôm vừa vặn, tôn dáng. Chất vải không bị nhăn. Highly recommended!', '2025-11-10 12:30:00', 5),
(18, 6, 2, 'Hien Nguyen', 'Blazer chất lượng, thiết kế tinh tế. Phù hợp cho nữ công sở. Giá hợp lý.', '2025-11-11 17:20:00', 4),

-- Comments for Quần Jeans Nam Slim Fit (Product 7)
(19, 7, 3, 'Loc Nguyen', 'Quần jeans chất denim cotton co giãn rất thoải mái. Form slim fit ôm vừa phải, không bị bó. Màu wash đẹp tự nhiên.', '2025-11-12 10:45:00', 5),
(20, 7, 6, 'Thanh Nguyen', 'Jeans đẹp, form chuẩn. Chất vải dày dặn, bền. Mặc đi làm hay đi chơi đều ok. Good quality!', '2025-11-13 14:20:00', 5),
(21, 7, 7, 'Tai Tran', 'Quần jeans slim fit rất đẹp. Phom dáng ôm vừa, tôn chân. Chất jean tốt, giặt không bị ra màu.', '2025-11-14 09:30:00', 5),

-- Comments for Quần Kaki Nam Chinos (Product 8)
(22, 8, 2, 'Hien Nguyen', 'Quần kaki chinos mặc rất thoải mái. Chất cotton twill mềm mại, form straight leg dễ mặc. Màu be rất đẹp!', '2025-11-10 11:15:00', 5),
(23, 8, 3, 'Loc Nguyen', 'Chất vải tốt, form đẹp. Phù hợp nhiều dịp từ đi làm đến đi chơi. Giá cả hợp lý. Will buy more!', '2025-11-11 16:40:00', 4),

-- Comments for Quần Short Nam Thể Thao (Product 9)
(24, 9, 6, 'Thanh Nguyen', 'Short thể thao chất polyester thấm hút mồ hôi tốt. Có lót bên trong tiện lợi, túi zip để điện thoại an toàn. Perfect cho gym!', '2025-11-12 07:30:00', 5),
(25, 9, 7, 'Tai Tran', 'Quần short mặc tập gym rất ok. Chất vải nhẹ, thoáng mát. Thiết kế túi zip tiện dụng. Recommended!', '2025-11-13 18:20:00', 5),

-- Comments for Quần Jeans Nữ Skinny (Product 10)
(26, 10, 4, 'Ly Nguyen', 'Quần jeans skinny ôm dáng hoàn hảo! Chất denim co giãn 4 chiều rất thoải mái. Tôn chân dài, rất đẹp.', '2025-11-14 15:30:00', 5),
(27, 10, 5, 'Thuy Nguyen', 'Jeans skinny đẹp lắm! Form ôm vừa vặn, không bị bó chặt. Chất jean mềm, co giãn tốt. Love it so much!', '2025-11-15 12:45:00', 5),
(28, 10, 2, 'Hien Nguyen', 'Quần jeans chất lượng tốt. Skinny fit nhưng vẫn thoải mái. Màu xanh đậm rất đẹp, dễ phối đồ.', '2025-11-16 10:20:00', 5),

-- Comments for Chân Váy Chữ A Vintage (Product 11)
(29, 11, 4, 'Ly Nguyen', 'Chân váy chữ A xinh xắn, phong cách vintage đáng yêu. Chất cotton pha mềm mại, độ dài qua gối thanh lịch.', '2025-11-11 14:10:00', 5),
(30, 11, 5, 'Thuy Nguyen', 'Váy đẹp, form xoè nhẹ rất nữ tính. Mặc đi làm hoặc đi chơi đều phù hợp. Chất vải tốt, không bị nhăn.', '2025-11-12 16:30:00', 4),

-- Comments for Quần Culottes Nữ Wide Leg (Product 12)
(31, 12, 4, 'Ly Nguyen', 'Quần culottes wide leg rất thoải mái! Chất polyester mềm mát, phù hợp thời tiết nóng. Form dáng đẹp, trendy!', '2025-11-13 11:50:00', 5),
(32, 12, 2, 'Hien Nguyen', 'Quần wide leg mặc mát mẻ, thoải mái. Form dáng đẹp, dễ phối áo. Chất vải nhẹ, không bị nhăn. Recommended!', '2025-11-14 13:40:00', 5),

-- Comments for Giày Sneaker Nam Basic (Product 13)
(33, 13, 3, 'Loc Nguyen', 'Giày sneaker basic đi rất êm chân! Upper da synthetic bền bỉ, đế rubber chống trượt tốt. Style minimalist dễ phối đồ.', '2025-11-15 09:25:00', 5),
(34, 13, 6, 'Thanh Nguyen', 'Sneaker đẹp, chất lượng tốt. Đi êm chân, không bị đau. Phong cách basic phù hợp nhiều outfit. Worth the price!', '2025-11-16 14:15:00', 5),
(35, 13, 7, 'Tai Tran', 'Đã mua đôi thứ 2 ở shop. Chất lượng ổn định, giao hàng nhanh. Giày đi thoải mái cả ngày không mỏi chân.', '2025-11-17 11:30:00', 5),

-- Comments for Giày Boot Nữ Chelsea (Product 14)
(36, 14, 4, 'Ly Nguyen', 'Boot chelsea rất sang trọng! Chất da PU cao cấp, gót 5cm vừa phải. Mặc đi làm hoặc dự tiệc đều đẹp.', '2025-11-10 16:40:00', 5),
(37, 14, 5, 'Thuy Nguyen', 'Giày boot đẹp lắm! Form cổ điển, dễ phối đồ. Gót cao vừa vặn, đi thoải mái. Quality tốt!', '2025-11-11 12:20:00', 4),

-- Comments for Sandal Nữ Đế Xuồng (Product 15)
(38, 15, 4, 'Ly Nguyen', 'Sandal đế xuồng phong cách bohemian xinh xắn. Quai ngang mềm mại, đế cao 7cm ổn định. Mặc đi chơi mùa hè rất ok!', '2025-11-12 15:10:00', 5),
(39, 15, 2, 'Hien Nguyen', 'Sandal đẹp, đế xuồng đi thoải mái. Không bị đau chân sau vài giờ đi. Thiết kế bohemian rất trendy.', '2025-11-13 10:50:00', 5),

-- Comments for Túi Tote Canvas Unisex (Product 16)
(40, 16, 3, 'Loc Nguyen', 'Túi tote canvas phong cách vintage đẹp! Chất canvas dày dặn, bền. Size vừa phải, đựng được laptop và đồ hàng ngày.', '2025-11-14 09:40:00', 5),
(41, 16, 6, 'Thanh Nguyen', 'Túi tote tiện lợi, chất canvas tốt. Dùng đi học hoặc đi làm đều phù hợp. Giá cả hợp lý. Recommended!', '2025-11-15 14:30:00', 5),
(42, 16, 7, 'Tai Tran', 'Túi canvas unisex rất practical. Chất liệu bền, form đẹp. Đựng đồ nhiều, quai xách chắc chắn. Good buy!', '2025-11-16 11:15:00', 4),

-- Comments for Nón Snapback Streetwear (Product 17)
(43, 17, 3, 'Loc Nguyen', 'Nón snapback chất cotton twill đẹp. Logo thêu nổi 3D chất lượng, viền nón cong phong cách Mỹ. Streetwear vibes!', '2025-11-11 13:20:00', 5),
(44, 17, 6, 'Thanh Nguyen', 'Snapback đẹp, chất lượng ok. Đội mát mẻ, thoáng khí. Logo thêu sắc nét. Perfect accessory!', '2025-11-12 17:45:00', 5),

-- Comments for Thắt Lưng Da Nam (Product 18)
(45, 18, 7, 'Tai Tran', 'Thắt lưng da thật genuine leather chất lượng. Mặt khóa kim loại cao cấp, thiết kế cổ điển sang trọng. Dùng lâu bền!', '2025-11-13 10:10:00', 5),
(46, 18, 2, 'Hien Nguyen', 'Belt da đẹp, chất lượng tốt. Khóa kim loại chắc chắn. Phù hợp đi làm văn phòng. Worth the investment!', '2025-11-14 15:35:00', 5),

-- Comments for Áo Polo Nam Classic (Product 19)
(47, 19, 3, 'Loc Nguyen', 'Áo polo cổ bẻ classic rất đẹp. Chất cotton pha thoáng mát, form regular fit thoải mái. Mặc đi làm hay đi chơi đều ok!', '2025-11-15 11:40:00', 5),
(48, 19, 6, 'Thanh Nguyen', 'Polo nam chất lượng. Thiết kế cổ bẻ truyền thống, form chuẩn. Vải mát, dễ giặt. Recommended!', '2025-11-16 09:20:00', 4),

-- Comments for Áo Len Nam Sweater (Product 20)
(49, 20, 2, 'Hien Nguyen', 'Áo len sweater ấm áp mùa đông. Chất acrylic mềm mại, form oversize trendy. Mặc rất thoải mái và ấm!', '2025-11-10 14:50:00', 5),
(50, 20, 7, 'Tai Tran', 'Sweater đẹp, chất len tốt không bị xù. Cổ lọ ấm cổ, form oversize vừa ý. Perfect for winter!', '2025-11-11 16:10:00', 5);

-- Insert orders (Đơn hàng đã hoàn tất - DELIVERED)
-- Cấu trúc: id, user_id, created_at, status, total, subtotal, discount_amount, shipping_fee, payment_method, shipping_address, phone_number, recipient_name, notes
INSERT IGNORE INTO orders (id, user_id, created_at, status, total, subtotal, discount_amount, shipping_fee, payment_method, shipping_address, phone_number, recipient_name, notes) VALUES

-- Đơn hàng của Hien Nguyen (user_id: 2) - 3 đơn
(1, 2, '2025-10-15', 'DELIVERED', 547300, 598000, 50700, 30000, 'CASH', '123 Đường Lê Lợi, Phường Bến Thành, Quận 1, TP.HCM', '0901234567', 'Hien Nguyen', 'Giao hàng giờ hành chính'),
(2, 2, '2025-10-25', 'DELIVERED', 784500, 858000, 73500, 0, 'MOMO', '123 Đường Lê Lợi, Phường Bến Thành, Quận 1, TP.HCM', '0901234567', 'Hien Nguyen', 'Gọi trước khi giao'),
(3, 2, '2025-11-05', 'DELIVERED', 1289300, 1329000, 69700, 30000, 'CARD', '123 Đường Lê Lợi, Phường Bến Thành, Quận 1, TP.HCM', '0901234567', 'Hien Nguyen', NULL),

-- Đơn hàng của Loc Nguyen (user_id: 3) - 3 đơn
(4, 3, '2025-10-18', 'DELIVERED', 629250, 599250, 0, 30000, 'CASH', '456 Đường Trần Hưng Đạo, Phường 5, Quận 5, TP.HCM', '0912345678', 'Loc Nguyen', 'Để hàng tại bảo vệ nếu không có người'),
(5, 3, '2025-11-01', 'DELIVERED', 949200, 949200, 0, 0, 'ZALOPAY', '456 Đường Trần Hưng Đạo, Phường 5, Quận 5, TP.HCM', '0912345678', 'Loc Nguyen', NULL),
(6, 3, '2025-11-20', 'DELIVERED', 1524250, 1494250, 0, 30000, 'MOMO', '456 Đường Trần Hưng Đạo, Phường 5, Quận 5, TP.HCM', '0912345678', 'Loc Nguyen', 'Ship nhanh giúp em'),

-- Đơn hàng của Ly Nguyen (user_id: 4) - 4 đơn
(7, 4, '2025-10-20', 'DELIVERED', 698150, 668150, 0, 30000, 'CASH', '789 Đường Nguyễn Thị Minh Khai, Phường 6, Quận 3, TP.HCM', '0923456789', 'Ly Nguyen', 'Giao buổi sáng'),
(8, 4, '2025-10-28', 'DELIVERED', 458350, 428350, 0, 30000, 'CARD', '789 Đường Nguyễn Thị Minh Khai, Phường 6, Quận 3, TP.HCM', '0923456789', 'Ly Nguyen', NULL),
(9, 4, '2025-11-08', 'DELIVERED', 1178300, 1148300, 0, 30000, 'MOMO', '789 Đường Nguyễn Thị Minh Khai, Phường 6, Quận 3, TP.HCM', '0923456789', 'Ly Nguyen', 'Kiểm tra hàng trước khi thanh toán'),
(10, 4, '2025-11-25', 'DELIVERED', 854250, 824250, 0, 30000, 'ZALOPAY', '789 Đường Nguyễn Thị Minh Khai, Phường 6, Quận 3, TP.HCM', '0923456789', 'Ly Nguyen', NULL),

-- Đơn hàng của Thuy Nguyen (user_id: 5) - 3 đơn
(11, 5, '2025-10-22', 'DELIVERED', 827500, 797500, 0, 30000, 'CASH', '321 Đường Cách Mạng Tháng 8, Phường 10, Quận 3, TP.HCM', '0934567890', 'Thuy Nguyen', NULL),
(12, 5, '2025-11-03', 'DELIVERED', 1298000, 1268000, 0, 30000, 'CARD', '321 Đường Cách Mạng Tháng 8, Phường 10, Quận 3, TP.HCM', '0934567890', 'Thuy Nguyen', 'Giao hàng cẩn thận'),
(13, 5, '2025-11-18', 'DELIVERED', 629450, 599450, 0, 30000, 'MOMO', '321 Đường Cách Mạng Tháng 8, Phường 10, Quận 3, TP.HCM', '0934567890', 'Thuy Nguyen', NULL),

-- Đơn hàng của Thanh Nguyen (user_id: 6) - 3 đơn
(14, 6, '2025-10-30', 'DELIVERED', 978200, 948200, 0, 30000, 'CASH', '654 Đường Võ Văn Tần, Phường 6, Quận 3, TP.HCM', '0945678901', 'Thanh Nguyen', 'Giao giờ hành chính'),
(15, 6, '2025-11-10', 'DELIVERED', 1447250, 1417250, 0, 30000, 'ZALOPAY', '654 Đường Võ Văn Tần, Phường 6, Quận 3, TP.HCM', '0945678901', 'Thanh Nguyen', NULL),
(16, 6, '2025-11-28', 'DELIVERED', 749300, 719300, 0, 30000, 'CARD', '654 Đường Võ Văn Tần, Phường 6, Quận 3, TP.HCM', '0945678901', 'Thanh Nguyen', 'Gọi trước 30 phút'),

-- Đơn hàng của Tai Tran (user_id: 7) - 4 đơn
(17, 7, '2025-10-12', 'DELIVERED', 1198000, 1168000, 0, 30000, 'MOMO', '987 Đường Hai Bà Trưng, Phường Đa Kao, Quận 1, TP.HCM', '0956789012', 'Tai Tran', NULL),
(18, 7, '2025-11-02', 'DELIVERED', 679450, 649450, 0, 30000, 'CASH', '987 Đường Hai Bà Trưng, Phường Đa Kao, Quận 1, TP.HCM', '0956789012', 'Tai Tran', 'Để tại bảo vệ nếu vắng'),
(19, 7, '2025-11-15', 'DELIVERED', 1548300, 1518300, 0, 30000, 'CARD', '987 Đường Hai Bà Trưng, Phường Đa Kao, Quận 1, TP.HCM', '0956789012', 'Tai Tran', NULL),
(20, 7, '2025-11-30', 'DELIVERED', 858450, 828450, 0, 30000, 'ZALOPAY', '987 Đường Hai Bà Trưng, Phường Đa Kao, Quận 1, TP.HCM', '0956789012', 'Tai Tran', 'Giao trong giờ hành chính');

-- Insert order_items (Chi tiết đơn hàng)
-- Cấu trúc: id, order_id, product_id, stock_id, quantity, unit_price, sub_total, color, size
INSERT IGNORE INTO order_items (id, order_id, product_id, stock_id, quantity, unit_price, sub_total, color, size) VALUES

-- Order 1 của Hien (Áo Thun Nam + Giày Sneaker) - Total: 598000
(1, 1, 1, 1, 2, 179000, 358000, 'Đen', 'L'),
(2, 1, 13, 2, 1, 799000, 240000, 'Trắng', '42'),

-- Order 2 của Hien (Áo Hoodie + Quần Jeans Nam) - Total: 858000
(3, 2, 3, 1, 1, 499000, 309000, 'Xám', 'L'),
(4, 2, 7, 1, 1, 549000, 549000, 'Xanh Đậm', '30'),

-- Order 3 của Hien (Áo Sơ Mi + Quần Kaki + Giày Boot Nữ) - Total: 1329000
(5, 3, 2, 1, 2, 399000, 798000, 'Trắng', 'M'),
(6, 3, 8, 2, 1, 419000, 252000, 'Be', '30'),
(7, 3, 14, 1, 1, 899000, 279000, 'Đen', '37'),

-- Order 4 của Loc (Giày Sneaker + Túi Tote) - Total: 599250
(8, 4, 13, 1, 1, 799000, 340000, 'Đen', '41'),
(9, 4, 16, 3, 1, 259000, 259250, 'Đen', 'Free Size'),

-- Order 5 của Loc (Áo Hoodie + Áo Polo + Quần Jeans) - Total: 949200
(10, 5, 3, 2, 1, 499000, 499000, 'Đen', 'XL'),
(11, 5, 19, 1, 1, 319000, 319000, 'Navy', 'L'),
(12, 5, 7, 1, 1, 549000, 131200, 'Xanh Nhạt', '31'),

-- Order 6 của Loc (Áo Khoác Jacket + Quần Tây + Ví Da) - Total: 1494250
(13, 6, 21, 1, 1, 799000, 799000, 'Đen', 'L'),
(14, 6, 35, 2, 1, 599000, 599000, 'Navy', '31'),
(15, 6, 46, 1, 1, 499000, 96250, 'Nâu', 'Free Size'),

-- Order 7 của Ly (Áo Blouse + Chân Váy + Giày Boot) - Total: 668150
(16, 7, 5, 1, 1, 459000, 459000, 'Trắng', 'M'),
(17, 7, 11, 2, 1, 359000, 209150, 'Đen', 'M'),

-- Order 8 của Ly (Áo Crop Top + Quần Culottes) - Total: 428350
(18, 8, 4, 1, 2, 199000, 398000, 'Hồng', 'M'),
(19, 8, 12, 1, 1, 479000, 30350, 'Trắng', 'L'),

-- Order 9 của Ly (Quần Jeans Nữ + Áo Blazer + Sandal) - Total: 1148300
(20, 9, 10, 1, 1, 599000, 599000, 'Xanh Đậm', '27'),
(21, 9, 6, 2, 1, 649000, 649000, 'Navy', 'M'),

-- Order 10 của Ly (Áo Cardigan Nữ + Sweater Nữ + Túi Xách) - Total: 824250
(22, 10, 26, 1, 1, 499000, 499000, 'Đen', 'M'),
(23, 10, 30, 2, 1, 459000, 325250, 'Xám', 'L'),

-- Order 11 của Thuy (Áo Thun Nữ Oversize + Quần Short Nữ + Sandal) - Total: 797500
(24, 11, 28, 1, 2, 259000, 518000, 'Trắng', 'L'),
(25, 11, 39, 2, 1, 349000, 279500, 'Đen', 'M'),

-- Order 12 của Thuy (Áo Trench Coat + Quần Tây Nữ) - Total: 1268000
(26, 12, 27, 1, 1, 969000, 969000, 'Be', 'L'),
(27, 12, 38, 2, 1, 549000, 299000, 'Xám', '28'),

-- Order 13 của Thuy (Giày Cao Gót + Túi Xách + Khăn Choàng) - Total: 599450
(28, 13, 42, 1, 1, 689000, 344500, 'Đen', '38'),
(29, 13, 45, 2, 1, 359000, 254950, 'Be', 'Free Size'),

-- Order 14 của Thanh (Áo Vest Nam + Quần Tây Nam + Thắt Lưng) - Total: 948200
(30, 14, 22, 1, 1, 649000, 649000, 'Navy', 'L'),
(31, 14, 35, 1, 1, 599000, 299200, 'Xám', '30'),

-- Order 15 của Thanh (Áo Len Sweater + Áo Cardigan Nam + Nón) - Total: 1417250
(32, 15, 20, 2, 1, 549000, 549000, 'Xám', 'L'),
(33, 15, 24, 1, 1, 599000, 599000, 'Be', 'XL'),
(34, 15, 17, 3, 1, 219000, 269250, 'Đen', 'Free Size'),

-- Order 16 của Thanh (Quần Jogger + Quần Cargo + Giày Thể Thao) - Total: 719300
(35, 16, 33, 1, 1, 399000, 399000, 'Đen', 'L'),
(36, 16, 34, 2, 1, 499000, 320300, 'Xám', 'L'),

-- Order 17 của Tai (Áo Khoác Jacket + Quần Jeans + Giày Lười) - Total: 1168000
(37, 17, 21, 2, 1, 799000, 799000, 'Nâu', 'XL'),
(38, 17, 7, 1, 1, 549000, 369000, 'Đen', '32'),

-- Order 18 của Tai (Áo Thun Graphic + Quần Short + Sandal Nam) - Total: 649450
(39, 18, 23, 1, 2, 239000, 478000, 'Đen', 'L'),
(40, 18, 9, 2, 1, 259000, 171450, 'Navy', 'L'),

-- Order 19 của Tai (Áo Vest + Quần Tây + Giày Lười + Ví Da) - Total: 1518300
(41, 19, 22, 2, 1, 649000, 649000, 'Đen', 'L'),
(42, 19, 35, 1, 1, 599000, 599000, 'Navy', '31'),
(43, 19, 41, 1, 1, 899000, 270300, 'Nâu', '42'),

-- Order 20 của Tai (Áo Polo + Quần Kaki + Kính Mát + Thắt Lưng) - Total: 828450
(44, 20, 19, 2, 2, 319000, 638000, 'Trắng', 'L'),
(45, 20, 8, 1, 1, 419000, 190450, 'Be', '31');
