import sqlite3

PATH_TO_DATABASE = "data/bubble.db"

conn = sqlite3.connect(PATH_TO_DATABASE)
c = conn.cursor()
c.execute("PRAGMA foreign_keys = ON")

DROP_NAMES_QUERY = '''DROP TABLE IF EXISTS "names";'''
DROP_SCORES_QUERY = '''DROP TABLE IF EXISTS "scores";'''

CREATE_NAMES_QUERY = '''CREATE TABLE IF NOT EXISTS names (
    id INT PRIMARY KEY NOT NULL,
    name VARCHAR(50)
);'''

CREATE_SCORES_QUERY = '''CREATE TABLE IF NOT EXISTS scores (
    id INT,
    game_id INT,
    game_date VARCHAR(50),
    score1 INT,
    score2 INT,
    score3 INT,
    score4 INT,
    score5 INT,
    score6 INT,
    score7 INT,
    score8 INT,
    score9 INT,
    total_score INT,
    comments VARCHAR(1000),
    FOREIGN KEY (id) REFERENCES names(id),
    PRIMARY KEY (id, game_id)
);'''

INSERT_NEWSPAPER = '''INSERT OR IGNORE INTO names VALUES (?, ?);'''
INSERT_FOLLOWER = '''INSERT OR IGNORE INTO scores VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);'''



def create_tables():
    """Creates three new SQL tables and overwrites old tables.

    1. accounts: handle, account name, int 0 to 2 where 0 = newspaper, 1 = RI politician, 2 = national politician
    2. newspaper_following: handle, account name (all accounts that follow newspapers in accounts table)
    3. political_following: handle, account name (all newspaper followers that follow politicians)"""


    c.execute(CREATE_NAMES_QUERY)
    c.execute(CREATE_SCORES_QUERY)
    conn.commit()


def main():
    # Create tables
    create_tables()




if __name__ == "__main__":
    main()



