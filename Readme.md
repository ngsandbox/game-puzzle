Run web application by the command `./gradlew bootRun`
 
Description: 
* By the URL http://localhost:8080 you have to provide your nick name for registration
* Enter the stats of your character, if this is the first login
* On the next (Game) page you can see your stats and fighting history and the button "Fight". 
* On the Fght page will be the small battle between your and random character. 
* After the battle, if you win, experience and victim will be display on the Game page.  


> Note: random character control is not automated. 


Implementation details:
* SpeciesService, CharacteristicService, ArenaService - domain level services for species generation, characteristics and other calculation  
* GameDAOImpl - database layer
* GameProperties - application level setting 