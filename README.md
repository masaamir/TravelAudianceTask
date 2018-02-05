# TravelAudianceTask

The program takes 5 input parameters
1- String: Input file path of csv in String (uuid,ts,useragent,hashed_ip)
2- String: Output file path csv (uuid,ts,useragent,hashed_ip, active user, Weekday/ Office hours Bizzer)
3- Int:  minimum Events/activities: A good value be 100
4- Int: For being an active user average time difference between activity in hours: A good value be 1 hour
5- Double: For being a OfficeTime bizzer the percantage of activities which should be done during office hours


#Active Users:

In order to consider a user as an active user, its average time difference between consecutive activities/ events should be less than a particular threshold. In this case, we take such a value from user, a good value can be considered 1 hours to find hourly (average) active users. To avoid the newcomers we ignore the users who have less than a particular number of activities/ events. This value is taken as an input from the user.


#Weekday Bizzers:

In order to find out that if a user's traffic mostly comes during office hours, I find out the percentage of his traffic during office hours. All the users having traffic greater than a threshold input taken by user, would be considered weekday bizzers. Considering the lesiure time this number should be greater than 0.7.



