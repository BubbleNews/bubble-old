# cs0320 Term Project 2020

**Team Members:**
- Kshitij Sachan
    - Strength: Object Oriented Design
    - Weakness: Bad at planning/time estimation
- John Graves
    - Strength: Large Scale Design
    - Weakness: Using documentation/syntax
- Ben Silverman
    - Strength: Planning/Outlining
    - Weakness: API stuff, GUI stuff
- Ian Layzer
    - Strength: OOP, coding quickly
    - Weakness: inexperience with front-end

**Team Strengths and Weaknesses:** 
- Based on the above strengths and weaknesses, it seems like one of our main
weaknesses is general inexperience with front-end GUI stuff, while our main
strength is overall program design.

##Project Ideas
### Idea 1: Social Media Aggregator
I’d love it if there was a social media app/site that did two things: 
- aggregated all social media platforms into one, meaning you can sign into Facebook, 
Instagram, Twitter, etc and see it all at once. 
- This is the most important part. It should cut out the bad stuff.
 It should cut out algorithms designed to keep your attention, tracking, all those stupid videos that get shared like crazy on Facebook, etc... Basically the point should be that with this site you can feel like you are staying up to date and connected with friends, but unlike social media, we don’t try to keep you addicted and tracked. It would be designed to be light. In addition, we could implement many features to help give the user control and create a more lightweight experience, the following are some examples:
    - We could have a feature to hide the like count on other people’s posts
    - We could have a mode where you just see stuff from friends that you specify you care about and all the rest is hidden

Rejected - seems like a bunch of api calls with no interesting design

### Idea 2: Events at Brown
There should be a way for students to see all the events that are happening on a weekend. 
For example, someone could look for all the a cappella concerts to take their parents 
to on parents weekend or they could look for all of the parties on a given night.
Features:
- Facebook/gmail authentication - Facebook would be useful so you can see what events 
your friends are going to and gmail would be useful so the events are only available 
to Brown students. I think it will be challenging to link up the Facebook API to our 
website because I don’t know how authentication works.
- Search functionality by name, type of event, and date. 
People need to be able to search events to get more detailed info on an event and 
see if their friends are going. This most challenging part would be setting up the 
comparators/finding the appropriate data structures to make searches fast
- A dynamic user feed that displays events that a user might be interested in based 
on their friends/past events they’ve attended. Ideally this would change whenever 
users refresh their feed, similar to how the Facebook feed works. Sponsored events 
might appear at the top of this feed. This would be very tricky to come up with 
a weighting algorithm for each event that is stochastic.

Approved - this will only be possible if you can find a LOT of users

### Idea 3: American News Polarization Indicator
As a country, we are becoming much more politically polarized, and the way we get our news is a major contributing factor. This website would help people become better informed about these issues by doing the following:

- Create an interactive map of the United States, breaking down the country into regions. Score regions on the partisanship of their news, by:
    - Getting the partisanship scores of relevant news sources (either from our Data Science final project, or from All-Sides)
    - Create an algorithm to estimate the news diet of a region using Google trends, twitter interactions, and circulation data 
        - Challenging to aggregate data and make effective model
        - Take in data from each of these sources in a variety of formats, an put it in an easily accessible format
        - Then a statistical model would be created to predict the percent of the new market each source takes up in a region
    - Map this data such that each region is hoverable/clickable 
        - Challenging since none of us are great with GUI
        - Each region would have a partisan score, and data on which news sources have an outsized impact in the region
        - The map could be overlaid with other data sets including:
            - Political results/turnout trends and results
            - UNC dataset on news deserts and newspaper density
            - Gallup Polarization data
- Create a personalized news profile for users, based on either:
    - User inputted data on how much they use different news sources
    - Requested authorized access to Facebook/Twitter (if time allows)
        - Difficult to use API and make effective algorithm
        - For each of these sources the site would:
            - Calculate an individual partisanship score 
            - Show which sources have the most sway on their news diet
            - Compare individual news diet to others in their region
- Potential Other Capabilities based on time (To fit the website to the needs of the data journalism summer project):
    - Ability to upload well formated articles and audio content related to news and partisanship and classify accordingly
    - Discussion board (piazza-esque) related to the journalism project on the decline of local news and it’s political effects
- Background: This could serve as the website for a data journalism summer project to share relevant analysis on news and polarization

Approved - lots of content so may need to trim some features

Note: You do not need to resubmit your final project ideas.

**Mentor TA:** _Put your mentor TA's name and email here once you're assigned one!_

## Meetings
_On your first meeting with your mentor TA, you should plan dates for at least the following meetings:_

**Specs, Mockup, and Design Meeting:** _(Schedule for on or before March 13)_

**4-Way Checkpoint:** _(Schedule for on or before April 23)_

**Adversary Checkpoint:** _(Schedule for on or before April 29 once you are assigned an adversary TA)_

## How to Build and Run
_A necessary part of any README!_
