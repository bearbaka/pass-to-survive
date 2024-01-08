<h1 align=center>🎮 Pass To Survive</h1>

<p align="center">

<img alt="GitHub Created At" src="https://img.shields.io/github/created-at/adiyat-abubakirov/pass-to-survive?style=for-the-badge">
<img alt="GitHub Release" src="https://img.shields.io/github/v/release/adiyat-abubakirov/pass-to-survive?sort=date&display_name=release&style=for-the-badge">
<img alt="GitHub repo size" src="https://img.shields.io/github/repo-size/adiyat-abubakirov/pass-to-survive?style=for-the-badge">
<img alt="GitHub License" src="https://img.shields.io/github/license/adiyat-abubakirov/pass-to-survive?style=for-the-badge">

</p>

## :page_facing_up: Description

This game is a demo of my skills, knowledge with Java & [libGDX](https://github.com/libgdx/libgdx).\
**Stack: Java 21, Gradle 8.11, [libGDX](https://github.com/libgdx/libgdx) 1.10, Lombok 1.18, Maven, Android Studio, [Tiled](https://doc.mapeditor.org/en/stable/manual/introduction/) (TMX Maps), LeakCanary 2.14.**

> [!NOTE]
>
> - For gameplay, see [gameplay.mp4](https://drive.google.com/file/d/1TkbdMnFe6whaoUqQKZORxapOZRVnNOiO/view?usp=sharing).
> - [libGDX](https://github.com/libgdx/libgdx) is compatible with any platform, including IOS & PC.
> - For project structure of Java classes, see [classes.png](.github/pictures/classes.png).

## :rocket: Key Features

- [Lombok](https://projectlombok.org/) for less code space, it automates creation of Builders, Setters, Getters, & many more.
- **Inheritance** from [`PlayGameScreen`](core/src/com/company/passtosurvive/levels/PlayGameScreen.java) abstract class with **Builder** & effective usage of abstract method `setCheckpoint` for [`Level`](core/src/com/company/passtosurvive/levels) Classes.
  - For [`PlayGameScreen`](core/src/com/company/passtosurvive/levels/PlayGameScreen.java), I created **Builder** myself b/c inheritance required `super`.
- **Inheritance** from[`TileObject`](core/src/com/company/passtosurvive/models/TileObject.java) abstract class, to make contactListener call `inContactAct` method to trigger actions corresponding to the specific [`TileObject` child](core/src/com/company/passtosurvive/models), regardless of what object is in contact with [`Player`](core/src/com/company/passtosurvive/models/Player.java).
- Created joystick by implementing [`Joystick`](core/src/com/company/passtosurvive/control/Joystick.java) class with Actor inheritance for communication with [libGDX](https://github.com/libgdx/libgdx) API & implementing [`JoystickInputListener`](core/src/com/company/passtosurvive/control/JoystickInputListener) class for touch control which communicates with App using [libGDX](https://github.com/libgdx/libgdx) API.
- Usage of diverse files to provide graphics, sound, animation, buttons, & text:
  - `.pack` - texture packs, tell the game in which part of a png file, the specific image is located.
  - [`.json`](assets/Buttons.json) - needed for all buttons in the game to set them a texture (pressed & released) from the `AllComponents.pack` file.
  - `.tmx` - maps that contains position for all the graphics & physical bodies on maps, use `.png` files as source of graphics.
- Doesn’t use Viewport to scale graphics, all graphics are optimized for different screens using just the coefficient of width & height to screen for which graphics was originally scaled. This **ensures original aspect of ratio for every graphics**. This technique is most often used in `resize()` methods. See [Example](.github/pictures/screens.png).

## Info

- Platformer with 2 levels (each has 2 parts) and 2 skins for the player model.
  - Second skin is applied after 20 restarts. After 20th restart, the game will show special animation for second skin instead of "Game Over" screen.
- Player's velocity is dependent on joystick's pull.
- There are 2 "Game Over" screens, 2 "Win" screens, and a "Pause" screen.
- The game works in native resolution and native refresh rate of the phone.

## Prerequisites

- #### Minimum Android Version: 4

---

<h3 align=center>If you found this repository helpful, please give it a :star:</h3>
