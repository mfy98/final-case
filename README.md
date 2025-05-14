Bu proje, Spring Boot 3, Java 21 ve PostgreSQL kullanarak geliştirilmiş, mikroservis mimarisine dayalı bir Kütüphane Yönetim Sistemi’dir. 
Ayrıca Spring WebFlux ile reaktif programlama, Docker ve Docker Compose ile konteynerizasyon, Swagger/OpenAPI ile API dokümantasyonu 
ve Postman ile hazır API test koleksiyonu gibi bonus özellikler de içermektedir.

Tech Stack

Java 21
Spring Boot 3
Spring WebFlux (Reactive)
Spring Data JPA & Hibernate
Spring Security + JWT
Spring Cloud Gateway (API Gateway)
PostgreSQL
Docker & Docker Compose
Swagger/OpenAPI
Postman (Collection dosyası dahil)
Maven (Build tool)
Logback/SLF4J ile merkezi loglama
H2 (Test ortamı için in-memory veritabanı)

-------------
Bu projede beş mikroservis bulunmaktadır:
auth-service Kullanıcı kimlik doğrulama ve JWT token üretimi.
user-service Kullanıcı CRUD işlemleri ve rollerin yönetimi.
book-service Kitap CRUD, arama ve pagination.
borrow-service Ödünç alma, iade, gecikme ve geri dönüt.
api-gateway Tüm istekleri yönlendiren merkezi gateway (Spring Cloud Gateway).

Her mikroservis ortak PostgreSQL veritabanına sahip ve Docker Compose ile birlikte ayağa kalkmakta.

![image](https://github.com/user-attachments/assets/75c5de06-7b0e-4d32-b5b2-e3abce2aa8e6)
![image](https://github.com/user-attachments/assets/21496f07-0f41-4d91-b487-84499c06db75)
