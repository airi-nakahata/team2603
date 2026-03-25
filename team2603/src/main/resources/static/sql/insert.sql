SET foreign_key_checks=0;

USE teamdb;

INSERT INTO mst_user
 (user_name, password, family_name, first_name, family_name_kana, first_name_kana, gender)
VALUES
 ('taro@gmail.com','111111','山田','太郎','やまだ','たろう',0);

INSERT INTO mst_category
 (category_name, category_description)
VALUES
 ('CD／映像','CDやDVDなどの映像商品'),
 ('ライブグッズ','ライブ関連グッズ'),
 ('コレクション','ランダム商品やコレクション');

INSERT INTO mst_product
 (product_name, product_name_kana, product_description, category_id, price, image_full_path, release_date, release_company)
VALUES
 ('1stシングル','ふぁーすとしんぐる','First Aid',1,1200,'/img/cd1.jpg','2024-08-10','Pulse Entertainment'),
 ('2ndシングル','せかんどしんぐる','HEALING YOUR HEART',1,1200,'/img/cd2.jpg','2024-12-24','Pulse Entertainment'),
 ('1stアルバム','ふぁーすとあるばむ','Remedy 4 You',1,3000,'/img/album1.jpg','2025-03-21','Pulse Entertainment'),
 ('ペンライト','ぺんらいと','ライブで使える公式ペンライト',2,2800,'/img/penlight.jpg','2024-08-10','Pulse Entertainment'),
 ('タオル','たおる','ライブグッズの定番タオル',2,2000,'/img/towel.jpg','2024-08-10','Pulse Entertainment'),
 ('うちわ','うちわ','メンバーソロ4種の応援うちわ',2,1200,'/img/uchiwa.jpg','2024-08-10','Pulse Entertainment'),
 ('アクリルスタンド','あくりるすたんど','ソロ4種＋集合1種',3,1500,'/img/acrylic.jpg','2024-08-10','Pulse Entertainment'),
 ('トレカ','とれか','2枚入りランダム全20種のトレーディングカード',3,600,'/img/card.jpg','2024-08-10','Pulse Entertainment'),
 ('缶バッジ','かんばっじ','ランダム全8種の缶バッジ',3,500,'/img/badge.jpg','2024-08-10','Pulse Entertainment');

SET foreign_key_checks=1;