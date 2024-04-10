# Do Today - Android Yapılacaklar Uygulaması


<img src="https://raw.githubusercontent.com/erendogan6/DoToday/main/app/src/main/res/drawable/logo.png" alt0="Logo" width="250" height="250">

## Proje Hakkında
DoToday, Kotlin ve Android Studio kullanılarak geliştirilmiş, günlük görevlerinizi yönetmenize yardımcı olmak için tasarlanmış bir yapılacaklar uygulamasıdır. Bu uygulama, Eisenhower Matrisi prensiplerini benimseyerek, görevlerinizi önem ve aciliyet durumlarına göre dört farklı kategoriye ayırmanıza olanak tanır. Bu özellik sayesinde, görevlerinize öncelik vermek ve zamanınızı daha etkin bir şekilde yönetmek sizin için çok daha kolay bir hale gelir.

<img src="https://github.com/erendogan6/DoToday/blob/main/DoToday%20App%20Using%20GIF.gif?raw=true" width="300">


## Özellikler
- Görev Ekleme: Yeni görevler ekleyebilir, her bir göreve başlık, açıklama, bitiş tarihi ve hatırlatma ayarlayabilirsiniz.
- Hatırlatıcı Bildirimler: Görevleriniz için belirlenen tarih ve saatte hatırlatıcı bildirimleri alabilirsiniz.
- Önceliklendirme: Görevlerinizi 'yüksek', 'orta', 'düşük' olmak üzere üç farklı öncelik seviyesine ayırabilirsiniz.
- Görevleri Kategorilere Ayırma: Görevleriniz 'Acil ve Önemli', 'Önemli Ama Acil Olmayan', 'Acil Ama Önemli Olmayan', 'Ne Acil Ne de Önemli' olmak üzere dört ana kategori altında yönetilir.
- Eisenhower Matrisi: Görevlerinizi önem ve aciliyet derecelerine göre sıralayarak, hangi görevlere odaklanmanız gerektiğini net bir şekilde görmenizi sağlar.
- Görev Düzenleme ve Silme: Var olan görevleri düzenleyebilir, gereksiz görevleri silebilirsiniz.
- Tamamlanma Oranı Gösterimi: Her iş listesi için, görevlerin tamamlanma oranını gösteren bir ilerleme çubuğu ile görevlerin ne kadarının tamamlandığını görebilirsiniz.

## Kullanılan Teknolojiler
- Kotlin
- MVVM (Model, View, Viewmodel) Architect
- SOLID Principles
- Coroutine
- Android Jetpack (Navigation, LiveData, ViewModel, Room Database)
- Hilt Dependency Injection
- UI/UX
- Fragment
- View Binding
- Recycler View
- Material Design Components
- Singleton Design Pattern

## Test Edilen Sürümler
- Android 9.0
- Android 11.0
- Android 13.0
- Android 14.0

## Kurulum

- Uygulamayı kullanmak için öncelikle Android Studio'yu bilgisayarınıza kurmanız gerekmektedir. Daha sonra aşağıdaki adımları takip edebilirsiniz:
- Bu repoyu yerel makinenize klonlayın:
```bash
git clone https://github.com/erendogan6/DoToday.git
```
- Android Studio'yu açın ve "Open an existing project" seçeneğini kullanarak indirdiğiniz projeyi seçin.
- Projeyi açtıktan sonra gereken bağımlılıkların indirilmesini bekleyin.
- Uygulamayı bir Android cihazda veya emülatörde çalıştırın.

- Uygulamayı doğrudan Android cihazınızda çalıştırabilmek için "DoToday.apk" dosyasını indirip uygulamayı cihazınıza kurabilirsiniz.


## DoToday Uygulaması Kullanım Kılavuzu

### Başlangıç
- Uygulamayı açtığınızda, çalışma listelerinizi görebileceğiniz ana ekran karşınıza çıkar. İlk adım olarak bir çalışma listesi oluşturmanız gerekir:

#### Çalışma Listesi Ekleme
- Ana ekranda bulunan "+ Ekle" butonuna tıklayın.
- Açılan diyalog kutusuna çalışma listenizin adını girin ve "Oluştur" butonuna basarak listenizi ekleyin.

#### Çalışma Listesi Yönetimi
- Oluşturduğunuz çalışma listeleri ana ekranda görüntülenir. Her bir listeye tıklayarak içeriğini görüntüleyebilir ve yönetebilirsiniz:

#### Görev Ekleme
- Çalışma listesinin içine girin.
- Sayfanın altındaki "+" (Ekle) butonuna tıklayarak yeni bir görev ekleyin.
- Açılan formda, görevinizle ilgili detayları doldurun: Görev adı, açıklama, bitiş tarihi, hatırlatıcı ve öncelik durumu.
- "Kaydet" butonuna basarak görevinizi listeye ekleyin.

#### Görev Yönetimi
- Her bir görevin yanında yer alan düzenleme ve silme ikonlarıyla görevler üzerinde değişiklik yapabilirsiniz.
- Görevleri tamamladıkça, yanındaki kutucuğu işaretleyerek görevinizi tamamlanmış olarak işaretleyebilirsiniz.

#### Eisenhover Matrisi
- DoToday, görevlerinizi önem ve aciliyet durumlarına göre kategorize edebileceğiniz Eisenhover Matrisi özelliğine de sahiptir. Bu özellik sayesinde, görevlerinizi "Acil ve Önemli", "Önemli Ama Acil Olmayan", "Acil Ama Önemli Olmayan" ve "Ne Acil - Ne de Önemli" olmak üzere dört farklı kategoride yönetebilirsiniz:
- Ana ekrandan "Eisenhower Matrisi" butonuna tıklayarak matrise erişebilirsiniz.
- Matriste, görevlerinizin hangi kategorilere ait olduğunu görebilir ve bu kategorilere tıklayarak ilgili görevleri listeleyebilirsiniz.

#### Hatırlatıcılar ve Öncelikler
- DoToday, görevlerinize hatırlatıcı eklemenize ve öncelik tanımlamanıza olanak tanır:
- Görev eklerken veya mevcut bir görevi düzenlerken, hatırlatıcı ve öncelik seçeneklerini belirleyebilirsiniz.
- Böylece, görevlerinizi zamanında gerçekleştirmeniz ve önem sırasına göre organize etmeniz kolaylaşır.

## Katkıda Bulunma ##

Projeye katkıda bulunmak isteyenler için katkı kuralları ve adımları CONTRIBUTING.md dosyasında açıklanmıştır.

##  Lisans ## 
Bu proje MIT Lisansı altında lisanslanmıştır.
