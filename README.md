# 🎸 record-shop-api 💰

## A RESTful api built with Spring Boot for an imagined record shop. Made as part of the Northcoders Java bootcamp.

`record-shop-api` is a project made to help a fictional business start to digitise their records. 
I was given a set of requirements (which can be found in `plan.png`, along with some small diagrams I made when planning the project) and told to complete them with what I had learned in the bootcamp so far. 
The project, although for a shop, didn't yet ask me to actually handle stock numbers and updates to this so this wasn't included in the initial solution - I have assumed that this will be a later addition. 
When building the project, I have tried to stick to a layered architecture as closely as possible and to use TDD when completing most of the work. 
In its current state, the project lets you spin up a server to connect to a PostgreSQL database, and creates the needed table to handle the album records.

## Table of Contents
- [Installation](#Installation)
- [Usage](#Usage)
    - [JSON Structure](#JSON-Structure)
    - [GET Requests](#GET-Requests)
    - [POST Requests](#POST-Requests)
    - [PUT Requests](#PUT-Requests)
    - [DELETE Requests](#DELETE-Requests)
- [Bugs and Contributions](#Bugs-and-Contributions)
- [Future Plans](#Future-Plans)


## Installation
Prerequisites: Ensure Java 21, PostgreSQL, and Maven are installed on your machine.
1) Clone this repo with `git clone https://github.com/sector-11/record-shop-api.git` in the directory of your choosing.
2) Ensure you have a PostgreSQL database on `localhost:5432/postgres` with a `postgres` user that has the password `admin` (these details can be changed by modifying `application-dev.properties` in the `src/main/resources` directory).
3) Ensure you have nothing running on port 8080, as this is needed to run the pre-packaging tests.
4) Open a terminal in the `/record-shop-api` directory and run `mvn package`. If all goes well, you should see a line like `Building jar: /path/to/directory/record-shop-api/target/Record-Shop-Api-VERSION.jar`. Please take note of the path and file mentioned here.
5) Move this .jar file to the directory of your choosing, and run it with the command `java -jar Record-Shop-Api-VERSION.jar`, replacing `VERSION` with whatever was shown by maven in the previous step.
6) You now have the program running! Press `CTRL + C` in the terminal window the program is running it to request a shutdown when you are done.


## Usage
In the current version, all requests to the database have a base path of `/api/v1/record-shop`, this will not be repeated in the below notes.
### JSON Structure
Your requests should keep in account the album JSON structure. A complete album record that will be returned looks like this:
```
{
    "id": Integer,
    "albumName": String,
    "artist": String,
    "releaseYear": Integer,
    "genre": String (of values from an enum)
}
```
When making requests, the `id` will be automatically generated and should not be included (most actions will be rejected if including an `id`).
The `genre` must have values of `"Pop", "Rock", "Hip-Hop", "RnB", "Country", "Jazz", "Metal", "Classical"`.

### GET Requests
A GET made to the `/records` endpoint will retrieve a list of all albums currently in the database.
You can also filter by the album's title, artist, genre, and release year with the parameters `title, artist, genre, and releaseYear` respectively.

Example usage: `/records?artist=ABBA&releaseYear=1979&genre=Pop&albumName=Voulez-Vous` should return an entry matching ABBA's 1979 smash-hit album Voulez-Vous, if it is in the database.

To find a specific entry in the database, you can perform a GET request to the `/records/{id}` endpoint, where `{id}` matches an entry in the database.

All GETs return a 200 status when successful, a 400 when done with invalid parameters (e.g. an invalid/null filter), and a 404 status when the requested resource cannot be found.

### POST Requests

POSTs are accepted on the `/records` endpoint. You must send a complete album (excluding `id`) as a JSON object in the request body or your request will be rejected.

If successful, you the server will return the created object and a 201 status. If you've given invalid input in the body, it will return a 400 status.

### PUT Requests

PUTs are made on the `/records/{id}` endpoint, where `{id}` is the id of the entry you would like to modify. The body of the request should include a JSON object with the fields you wish to modify.

Although these requests should be made as a POST, you can also PUT to the `/records` endpoint with a complete album in the body to create a new entry in the database.

If successfully modifying an entry, the server will return the updated object with a 200 status. When making a new entry, it will return a 201 status with the new object. If you attempt to modify an object that can't be found the server will return a 404, and if the request is invalid in any way it will return a 400 with an explanation why your request was rejected.

### DELETE Requests

DELETEs are requested on the `/records/{id}` endpoint, where `{id}` is the id of the entry you want to delete.

If your request is successfully processed, the server will return a 204 status. If the requested album couldn't be found to delete, it will return a 404 status. When a delete request is made without a valid id, the server will return a 400 status.


## Bugs and Contributions

If you find any bugs, please create an issue on the issues page of this repo, and I'll see if I can find a fix.

As this project was made as part of a bootcamp to show what I had learned, I'm not planning on accepting any contributions to the code at this time. Feel free to fork the project to play around with it if you wish though!


## Future Plans

I want to change the defaults when cloned to use an H2 in-memory database for ease of install when people are testing, as the project currently relies on PostgreSQL being installed and configured in a certain way on the end users machine.
Eventually, I also want to add a stock table and support for updating this - roughly following the layout given in `plan.png`.