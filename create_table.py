import sqlite3

PATH_TO_DATABASE = "data/bubble.db"

conn = sqlite3.connect(PATH_TO_DATABASE)
c = conn.cursor()
c.execute("PRAGMA foreign_keys = ON")





CREATE_ENTITY_QUERY = '''CREATE TABLE IF NOT EXISTS entity (
    id INT PRIMARY KEY NOT NULL,
    class VARCHAR(20),
    entity VARCHAR(50),
    count INT
);'''

CREATE_VOCAB_QUERY = '''CREATE TABLE IF NOT EXISTS vocab (
    word VARCHAR(50) PRIMARY KEY NOT NULL,
    count INT
);'''

CREATE_CLUSTERS_QUERY = '''CREATE TABLE IF NOT EXISTS clusters (
    id INT PRIMARY KEY NOT NULL,
    title VARCHAR(50),
    size INT,
    day INT,
    hour INT,
    avg_connections DOUBLE,
    avg_radius DOUBLE,
    std DOUBLE,
    intermediate_cluster BOOLEAN
);'''

CREATE_ARTICLES_QUERY = '''CREATE TABLE IF NOT EXISTS articles (
    id INT PRIMARY KEY NOT NULL,
    title VARCHAR(50),
    url VARCHAR(200),
    date DATETIME,
    text TEXT,
    final_cluster_id INT,
    temp_cluster_id INT,
    FOREIGN KEY (final_cluster_id) REFERENCES clusters(id),
    FOREIGN KEY (temp_cluster_id) REFERENCES clusters(id)
);'''

CREATE_ARTICLE_VOCAB_QUERY = '''CREATE TABLE IF NOT EXISTS article_vocab (
    article_id INT,
    vocab_id INT,
    count INT,
    FOREIGN KEY (article_id) REFERENCES articles(id),
    FOREIGN KEY (vocab_id) REFERENCES vocab(id)
);'''



CREATE_ARTICLE_ENTITY_QUERY = '''CREATE TABLE IF NOT EXISTS article_entity (
    article_id INT,
    entity_id INT,
    FOREIGN KEY (article_id) REFERENCES articles(id),
    FOREIGN KEY (entity_id) REFERENCES entity(id)
);'''

CREATE_EDGES_QUERY = '''CREATE TABLE IF NOT EXISTS edges (
    id INT PRIMARY KEY NOT NULL,
    article_id1 INT,
    article_id2 INT,
    weight DOUBLE,
    FOREIGN KEY (article_id1) REFERENCES articles(id),
    FOREIGN KEY (article_id2) REFERENCES articles(id)
);'''


def create_tables():
    """Creates three new SQL tables and overwrites old tables.

    1. accounts: handle, account name, int 0 to 2 where 0 = newspaper, 1 = RI politician, 2 = national politician
    2. newspaper_following: handle, account name (all accounts that follow newspapers in accounts table)
    3. political_following: handle, account name (all newspaper followers that follow politicians)"""


    
    c.execute(CREATE_ENTITY_QUERY)
    c.execute(CREATE_VOCAB_QUERY)
    c.execute(CREATE_CLUSTERS_QUERY)
    c.execute(CREATE_ARTICLE_VOCAB_QUERY)
    c.execute(CREATE_ARTICLES_QUERY)
    c.execute(CREATE_ARTICLE_ENTITY_QUERY)
    
    c.execute(CREATE_EDGES_QUERY)
    conn.commit()


def main():
    # Create tables
    create_tables()




if __name__ == "__main__":
    main()



