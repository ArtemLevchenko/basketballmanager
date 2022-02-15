# Tech Stack
* Java 17
* Spring Boot/MVC
* Lombok
* Thymeleaf
* Native JS + jQuery 3.2.1

#What is this?
NBA SIMULATION process with a real player stats and coach settings

# Help book
COACH:
1) ShootingFocusTendency insideOutside:
- only for schema

2)GameFocusTendency offenseDefense:
- For offensive coachWeight = (getBallSecurity() + getTotalRank() + coachWeight) / 2
- For defensive coachWeight = (getDefenseOnBall() + getTotalRank() + coachWeight) / 2
- Game tempo

PLAYER:
1) ShootingFocusTendency insideOutside:
- depends on schema will add bonuses
- include in offensive (for select in Attack) by coach schema
2) GameFocusTendency offenseDefense
- Add bonus points in Offensive and Defensive
3) SupportFocusTendency reboundAssist
- Include only to grab the Rebound or take assist
4) OffensiveFocusTendency screenOpening
- Add bonus points in Offensive by Schema (Max. points: inside - screen, balance - balance, 3pt - opening)

