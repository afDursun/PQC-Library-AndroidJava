# PQCLibary-AndroidJava

NIST Standartlaştırma sürecindeki kafes-tabanlı SABER, CRYSTAL-KYBER ve NTRU algoritmaları ile oluşturulmuştur. Android-Java ortamında test aşamaları yapılmıştır.


Kütüphanenin Kullanımı:
```java
PQCLibary pqcLibary = new PQCLibary("Algoritma_Etiketi");  // Anahtar Üretimi
 ```
 
 PQC-Library içerisinde kullanılabilecek etiketler aşağıda listelenmiştir.
 | Algoritma | PQC-Library Etiketi |
| --- | --- |
| LightSaber | Saber_Light |
| Saber | Saber |
| FireSaber | Saber_Fire |
| Kyber512 | Kyber_512 |
| Kyber768 | Kyber_768|
| ntruhps2048509 | NTRU_509 |
| ntruhps2048677 | NTRU_677 |
| ntruhps4096821 | NTRU_821 |
| ntruhrss701 | NTRU_701 |

Örnek Kullanım:
```java
PQCLibary pqcLibary = new PQCLibary("Kyber_512");  // Algoritma Belirleme
pqcLibary.KeyGen() // Anahtar Üretimi
EncapsulationModel enc = pqcLibary.Encapsulation(pqcLibary.pk); //Paketleme
byte[] sharedSecretKey = pqcLibary.Decapsulation(enc.getCipherText(),pqcLibary.sk); // Paket Çözme
 ```
Bu çalısma 121R006 proje numarasıyla TUBITAK tarafından desteklenmektedir.
