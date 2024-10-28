# HikariAPI

> HikariAPI is a shot API to manage connection to database.

- Name: HikariAPI
- Type: API
- Status: Done
- Developer(s): [KIZA](https://twitter.com/KIZAFOX)
- Specification (If any):


- Java Version: [JDK:22](https://www.oracle.com/fr/java/technologies/downloads/)

## Table of Contents

<!-- TOC -->
* [HikariAPI](#hikariapi)
  * [Table of Contents](#table-of-contents)
  * [Installation](#installation)
  * [Usage](#usage)
      * [Basic Setup](#basic-setup)
    * [Event Handling](#event-handling)
    * [Command Handling](#command-handling)
  * [Code Examples](#code-examples)
    * [Packets](#packets)
        * [_Message_](#_message_)
        * [_Title_](#_title_)
        * [_Action Bar_](#_action-bar_)
  * [Informations](#informations)
  * [License](#license)
<!-- TOC -->

---

## Installation

1. **Clone the repository:**

   ```bash
   git clone https://github.com/KIZAFOX/HikariAPI.git
   ```

2. **Build the project:**
    ```bash
   mvn clean install
    ```

3. **Import the .jar into your own project**

## Usage

#### Connect

```java
HikariAPI.connect("root", "", "localhost", 3306, "database");
```


#### Disconnect

```java
HikariAPI.disconnect();
```

---

## Code Examples

#### _Boolean_

```java
private static boolean hasAccount(final int id){
    return (boolean) new DBQuery(HikariAPI.getDbHandler().pool().getDataSource()).query((resultSet -> {
        try {
            if(resultSet.next()){
                return true;
            }
        } catch (final SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }), "SELECT id FROM users WHERE id='" + id + "'");
}
```

---

## Informations

- ↪️ I'm currently working on the documentation. Create an issues in case of a problem.
- ↪️ Contact me on [twitter](https://twitter.com/KIZAFOX) or [discord](https://discordapp.com/users/312654382586134529) if you have any questions.

## License

- ↪️ For more details, refer to the license text available [here](LICENSE).