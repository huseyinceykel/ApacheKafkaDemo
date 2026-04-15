# 🚀 Spring Boot & Apache Kafka: Lojistik Takip Demosu

Bu proje, Apache Kafka'nın temel taşlarından olan **Partitioning** ve **Key** kullanımını, gerçek bir lojistik senaryosu üzerinden deneyimlemek için hazırlanmıştır. Medium'daki ["Nedir Bu Apache Kafka?"](https://medium.com/@huseyinerenceykel/nedir-bu-apache-kafka-fd8c16f76f26) yazımdaki teorik bilgilerin somut bir uygulamasıdır.

## 🛠 Kullanılan Teknolojiler
- **Java 21** & **Spring Boot 3.5.x**
- **Apache Kafka** (Local Broker)
- **SpringDoc OpenAPI (Swagger UI)**: API testleri için.
- **Lombok**: Boilerplate kodları azaltmak için.

## 📦 Proje Bileşenleri

### 1. Topic Konfigürasyonu (`KafkaConfig.java`)
Kafka'nın dağıtık gücünü test etmek için `logistics-topic` adında, **3 partition**'lı bir yapı kurguladık. Bu sayede mesajların farklı partition'lara nasıl dağıldığını gözlemleyebiliyoruz.

### 2. Mesaj Gönderimi (Producer)
`/api/v1/kafka/publish` endpoint'i üzerinden mesajlarımızı bir **Key** (örn: `arac-101`) ile gönderiyoruz. Kafka, bu key'i kullanarak mesajın hangi partition'a gideceğine karar veriyor.

### 3. Mesaj Tüketimi (Consumer & Storage)
`KafkaConsumerService`, gelen mesajları gerçek zamanlı dinler ve bunları bir `ConcurrentHashMap` içinde saklar. Böylece hem konsoldan logları izleyebilir hem de API üzerinden geçmiş mesajları sorgulayabilirsin.

## 🧪 Uygulamalı Test Senaryoları (Demo Akışı)

Projeyi çalıştırdıktan sonra [Swagger UI](http://localhost:8080/swagger-ui.html) üzerinden şu testleri yapabilirsin:

### Senaryo A: Mesaj Sıralamasının Korunması (Same Key)
*   **İşlem:** `key=kurye-1` ile arka arkaya "Paket Alındı", "Yola Çıktı", "Teslim Edildi" mesajlarını gönder.
*   **Gözlem:** Tüm bu mesajların **aynı Partition ID**'ye (örn: Partition 2) düştüğünü göreceksin.
*   **Sonuç:** Kafka, aynı key'e sahip mesajları aynı partition'da tutarak işlem sırasını (Order Guarantees) korur.

### Senaryo B: Yük Dengeleme (Different Keys)
*   **İşlem:** Farklı araçlar için (`kurye-1`, `kurye-2`, `kurye-3`) mesajlar gönder.
*   **Gözlem:** Mesajların Partition 0, 1 ve 2 arasında dağıldığını göreceksin.
*   **Sonuç:** Farklı anahtarlar, yükün sistem genelinde dengeli dağılmasını (Load Balancing) sağlar.

## 🖥️ API Kullanımı

| Metot | Endpoint | Açıklama |
| :--- | :--- | :--- |
| `POST` | `/api/v1/kafka/publish` | Kafka'ya mesaj gönderir. (Parametreler: `key`, `message`) |
| `GET` | `/api/v1/kafka/messages` | Belirli bir key'e ait tüm mesajları getirir. |
| `GET` | `/api/v1/kafka/messages/all` | Sistemdeki tüm partition'lara dağılmış mesajları listeler. |

---

### 🚀 Nasıl Çalıştırılır?
1.  Lokalinizde bir Kafka Broker'ın (9092 portunda) çalıştığından emin olun.
2.  Projeyi klonlayın ve terminalde: `mvn spring-boot:run` komutunu çalıştırın.
3.  `http://localhost:8080/swagger-ui.html` adresine giderek Kafka dünyasına ilk adımınızı atın!

---

**Özetle:** Spring Boot ve Kafka ikilisi, mikroservis mimarilerinde asenkron iletişim kurmak için harika bir çözüm sunuyor. Bu demo projesiyle temel bir akışı kurmuş olduk. 

Sen de kendi projende Kafka'yı hangi senaryolarda kullanmayı düşünüyorsun? Yorumlarda buluşalım! 🚀
