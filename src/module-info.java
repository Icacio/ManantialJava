module ManantialJava {
	requires java.sql;
	requires java.desktop;
	requires transitive ManantialLang;
	exports com.example.manantial.modelo;
}