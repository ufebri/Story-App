<a href='https://play.google.com/store/apps/details?id=com.raytalktech.storyapp&pcampaignid=pcampaignidMKT-Other-global-all-co-prtnr-py-PartBadge-Mar2515-1'><img alt='Get it on Google Play' src='https://play.google.com/intl/en_us/badges/static/images/badges/en_badge_web_generic.png'  height="150"/></a>

Introduction
============
Stories. Capture your memories!

Screenshoot
===========
<img src="./asset/1.png" alt="image 1" height="350"/><img src="./asset/2.png" alt="image 2" height="350"/><img src="./asset/3.png" alt="image 3" height="350"/>
<img src="./asset/4.png" alt="image 4" height="350"/><img src="./asset/5.png" alt="image 5" height="350"/><img src="./asset/6.png" alt="image 6" height="350"/>
<img src="./asset/7.png" alt="image 7" height="350"/>

Google Map Things
-----------------
Make sure you guys has been created the key & enable Google Map API Key SDK for
Android [Google Cloud Console ](https://console.cloud.google.com/apis/credentials)

One more thing, you should get SHA-1 for development or debug using this line on your terminal or
cmd:

    keytool -list -v -keystore ~/.android/debug.keystore -alias androiddebugkey -storepass android -keypass android

for windows using this line

    keytool -list -v -keystore "%USERPROFILE%\.android\debug.keystore" -alias androiddebugkey -storepass android -keypass android

Put that fingerprint package & SHA-1 on Google Cloud Console
SHA-1 : 6C:23:6F:4E:1A:DF:B0:B9:76xxxxxx
Package : com.raytalktech.storyapp

After you put them, you need to add Google Map API Key like :
AIzaSyDALH-xxxxx
on the Manifest

and violaa... pinjem dulu seratus
<img src="./asset/maps.png"/>
