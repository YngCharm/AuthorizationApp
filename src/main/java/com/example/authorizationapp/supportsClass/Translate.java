package com.example.authorizationapp.supportsClass;

public class Translate {
    String[] rus = {".", "а", "б", "в", "г", "д", "е", "ё", "ж", "з", "и", "й", "к", "л", "м", "н", "о", "п", "р", "с", "т", "у",
    "ф", "х", "ц", "ч", "ш", "щ" , "", "", "", "э", "ю", "я"};
    String[] en = {".","a","b","v","g","d","e","e","zh","z","i","y","k","l","m","n","o","p","r","s","t","u","f","h",
            "c","ch", "sh", "", "", "", "e", "yu", "ya" };

    public String transcription(String firstname, String lastname, String patronymic){
        firstname = firstname.substring(0,1);
        patronymic = patronymic.substring(0,1);
        var originalUsername = (lastname + "." + firstname + "." + patronymic).toLowerCase();
        var username = new StringBuilder();

        String[] surnameChar = originalUsername.split("");

        for (String letter : surnameChar){
            for (var i=0; i < rus.length; i++) {
                if (rus[i].equals(letter)) {
                    username.append(en[i]);
                }
            }
        }
        System.out.println(username);
        return username.toString();
    }

}
