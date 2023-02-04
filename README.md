# Welcome to GeoGebra!


This repository contains source code of [GeoGebra](https://www.geogebra.org)'s math apps.
It is available on a private GitLab instance and mirrored to GitHub


Please read https://www.geogebra.org/license about GeoGebra's
licensing.

According to the license mentioned  at the link above, the source code of Geogebra is licensed under the [GNU Public License](https://www.gnu.org/licenses/gpl-3.0.html). The copyright notice is placed as usual in the LICENSE file of the repository. This repository
* adds the copyright notice for the GNU Public License with a license file for the GNU Public License
* Reference to the GNU Public License is: https://www.gnu.org/licenses/gpl-3.0.html
* the used [LICENSE]() is a plain text version of the [GNU Public License](https://www.gnu.org/licenses/gpl-3.0.txt)  URL: https://www.gnu.org/licenses/gpl-3.0.txt
* See also [Open Educational Resources](https://www.unesco.org/en/open-educational-resources) for collaborative working and sharing of OER documents and with application of the [FAIR principle](https://www.go-fair.org/fair-principles/)
* For current versions of Geogebra see https://github.com/geogebra/geogebra

## Other Open Source Tools for Mathematics
Geogebra is due to the OpenSource licensing model on of the core components of OpenSource mathematics tools.
* Maxima wxMaxima
* [Octave](https://octave.org/)
* [R Statistics](https://www.r-project.org/about.html) - [Wikipedia - R Programming Language](https://en.wikipedia.org/wiki/R_(programming_language)
* [SOFA](https://www.sofastatistics.com/home.php)
* [CAS4wiki](https://en.wikiversity.org/wiki/CAS4Wiki) is an [Algebrite](https://www.algebrite.org) and [Plotly.js](https://plotly.com/javascript/) based web-based Computer Algebra System, that was intended to be a rapid prototype for testing   


## Running the web version
To start the web version from command line, run

```
./gradlew :web:run
```

This will start a development server on your machine where you can test the app. 
If you need to access the server from other devices, you can specify a binding address

```
./gradlew :web:run -Pgbind=A.B.C.D
```

where `A.B.C.D` is your IP address. 
Then you can access the dev server through `http://A.B.C.D:8888`.
You can also run `./gradlew :web:tasks` to list other options.

## Running the desktop version (Classic 5)
To start the desktop version from command line, run

```
./gradlew :desktop:run
```
You can also run `./gradlew :desktop:tasks` to list other options.

## Setup the development environment

* Open IntelliJ. If you don't have IntelliJ on your computer yet 
then you can download and install it from [here](https://www.jetbrains.com/idea/download)
* In the menu select File / New / Project from Version Control / Git
* In the new window add the following path: `https://git.geogebra.org/ggb/geogebra.git`
* Click on ‘Checkout’, select your preferred destination folder, select Java 1.8 as the SDK, 
click on OK and wait…
* After the project is checked out, select the root folder of the project, 
open the Run Anything tool (Double ^ on Mac) and run the following command: 
`./gradlew :web:run`
* After a minute or two the GWT UI will appear
* After the Startup URLs are loaded on the UI, select the app that you wish start. For example, 
if you select `graphing.html` and click on Launch Default Browser 
then the Graphing Calculator app with the newest features 
will load and start in your default browser 
