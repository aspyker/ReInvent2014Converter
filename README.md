This project takes the raw screen scraping from the re:Invent session catalog website and creates
an index of session, title, speakers, abstract, links to YouTube and SlideShare.

You need to get an API access key for SlideShare for this to work.  You also need an API key for YouTube searches.
Once you have them edit application.conf.

Run with ./gradlew run > out.html

For now, I have checked in the code to JSlideShare into this github project.  Unfortunately the version
on google code doesn't seem to updated recently.  I'm doing to see if there is some way to get my patches
into it which consist of using the now required HTTPS and adding a creation date to Slideshare info.

In 2014, this was different from 2013 as I choose to rewrite in Scala thart year (as opposed to Groovy).  In 2015,
I adjusted just the minimum code required to get it to work (mostly a change to use Google V3 API's and client).
