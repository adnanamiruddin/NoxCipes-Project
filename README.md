![Logo](https://github.com/adnanamiruddin/NoxCipes-Project/blob/main/app/src/main/res/drawable/logo_noxcipes.png?raw=true)

# NoxCipes
NoxCipes merupakan aplikasi yang dirancang khusus untuk para pecinta kuliner vegetarian. Dengan NoxCipes, pengguna dapat menemukan berbagai resep masakan vegetarian yang lezat dan sehat, dilengkapi dengan bahan-bahan resep, instruksi langkah demi langkah yang mudah diikuti dan sebagainya.

## Fitur Utama
- **Koleksi Resep**: NoxCipes menyediakan berbagai resep masakan vegetarian.
- **Detail Resep**: Informasi lengkap termasuk bahan-bahan, langkah-langkah memasak, waktu persiapan, waktu memasak, dan jumlah porsi.
- **Navigasi yang Mudah**: Antarmuka intuitif dengan fitur pencarian untuk menemukan resep masakan.
- **Menyimpan Resep ke Daftar Favorit**: Tandai dan simpan resep favorit untuk diakses kembali dengan mudah.

## Cara Penggunaan Aplikasi
1. **Register**: Pada saat pertama kali aplikasi NoxCipes dibuka, maka akan diarahkan ke halaman utama. Silahkan klik text di bawah untuk langsung diarahkan ke halaman registrasi. Input semua informasi yang dibutuhkan (nama, email, password, dan konfirmasi password) lalu klik tombol "Submit & Login".
    - *Login*: Jika sudah memiliki akun, maka pada halaman pertama sebelumnya, langsung klik tombol "Login" untuk diarahkan ke halaman login NoxCipes.
2. **Home**: Halaman pertama yang diakses oleh user yang telah login ialah halaman beranda NoxCipes. Pada halaman beranda ini, NoxCipes akan menampilkan seluruh daftar resep masakan vegetarian dalam bentuk card.
    - *Recipe List Card*: Daftar resep masakan vegetarian di halaman beranda akan ditampilkan dalam bentuk card, di mana card-card tersebut memuat informasi seputar resep berupa gambar makanannya, nama makanannya, dan tingkat kesulitan proses pembuatan masakannnya. Card ini juga akan menampilkan icon favorit jika user menambahkan resep masakan tersebut ke dalam daftar resep masakan favoritnya.
3. **Recipe Detail**: Ketika card resep masakan tersebut diklik, maka akan diarahkan ke halaman detail dari resep masakan itu. Halaman detail resep akan menampilkan gambar makanan, nama makanan, tingkat kesulitan proses pembuatan masakan, deskripsi masakan, perkiraan waktu persiapan, waktu memasak, jumlah porsi, dan bahan-bahan yang dibutuhkan, serta langkah demi langkah proses pembuatan.
    - *Add to Favorite*: Selain informasi-informasi di atas, pada halaman detail resep ini juga terdapat tombol berbentuk "Love" untuk menambahkan resep masakan tersebut ke daftar resep masakan favorit user.
4. **Search Recipe**: NoxCipes juga menyediakan fitur untuk mencari resep berdasarkan nama resep ataupun berdasarkan tingkat kesulitannya.
5. **My Favorite Recipes**: Daftar resep masakan yang sudah ditambahkan oleh user dapat diakses pada halaman daftar resep favorit yang sudah disediakan oleh NoxCipes. Halaman ini dapat diakses melalui item navigasi berbentuk "Love" yang terletak di bawah.
6. **Profile**: Selain fitur utama seputar resep masakan vegetarian, NoxCipes juga menyediakan menu halaman profil agar user dapat memperbarui nama penggunanya di aplikasi, emailnya, hingga password akunnya.

### Implementasi Teknis
1. **Register and Login**: 
    - Data user yang melakukan registrasi akan disimpan di local database SQLite.
    - Pada saat user selesai registrasi maupun pada saat login, maka akan ada dua informasi yang disimpan pada SharedPreference. Yaitu:
        - "is_logged_in" dengan tipe data boolean. Berguna agar jika user sudah login pada NoxCipes lalu user menutup aplikasi, maka saat aplikasi dibuka kembali, user langsung diarahkan ke halaman utama NoxCipes (tidak perlu login lagi).
        - "user_id" dengan tipe data int. Berguna untuk menyimpan memori id user yang sedang login pada NoxCipes. Id ini dapat digunakan untuk mendapatkan informasi akun user, mendapatkan informasi id user pada saat menambah resep ke daftar favorit.
2. **Home**:
    - Implementasi menggunakan RecyclerView untuk menampilkan daftar resep, CardView untuk desain card, dan Picasso untuk memuat gambar makanannya dari API.
3. **Tambahkan ke Daftar Favorit**:
    - Menggunakan SQLite yang sudah dikonfigurasikan melalui class DbConfig. Implementasi melalui method `DbConfig.insertFavorite(int userId, int recipeId)` untuk menyimpan daftar resep favorit user.
    - Data yang disimpan di dalam tabel favorit mencakup user_id dan recipe_id.
4. **Pencarian Resep (Search Recipe)**:
    - Implementasi pengelolaan hasil pencarian melalui method `.searchRecipe(String query)` yang ada pada SearchRecipesAdapter. Menggunakan fungsi bawaan Java untuk mengelola persamaan text yang diinput dengan hasil filter data.
6. **My Favorite Recipes**:
    - Implementasi menggunakan RecyclerView untuk menampilkan daftar resep favorit dari database SQLite yang sudah dikonfigurasikan melalui class DbConfig.
    - Mendapatkan informasi daftar resep favorit apa saja yang dimiliki user melalu method `DbConfig.getFavoritesByUserId(int userId)`.
7. **Profile**:
    - Menggunakan SQLite yang sudah dikonfigurasikan melalui class DbConfig. Implementasi memperbarui data user melalui method `DbConfig.updateProfile(int userId, String name, String email, String password)`.
    - Ketika tombol "Logout" diklik, maka informasi pada SharedPreference akan diperbarui.
        - "user_id" -> null
        - "is_logged_in" -> false

## Teknologi yang Digunakan
- **Android SDK**: Aplikasi ini dibangun menggunakan Android SDK dan mengikuti pedoman desain material untuk antarmuka pengguna yang modern dan responsif.
- **Retrofit**: Untuk koneksi jaringan dan pengambilan data dari API.
- **Picasso**: Untuk pemuatan dan penanganan gambar.
- **SQLite**: Untuk penyimpanan data lokal.

## Authors
- [@adnanamiruddin](https://github.com/adnanamiruddin)

