# Telegram contest of April 7-15 2019, Android.
## Data
Mocks for task: Telegram-Task-2.studio (open in free [InVision Studio](https://www.invisionapp.com/))
Provided json files are under "./telegramchart/src/main/resources/contest" folder

![Demo](https://github.com/SJOwl/android-charts-telegram-2/blob/master/t2demo.webm)
[Download apk](https://github.com/SJOwl/android-charts-telegram-2/blob/master/app-release.apk)

## Task

Please find the input data for Stage 2 in the archive below. There are 5 folders there, each containing a .json for the corresponding graph (“overview.json”) and 12 subfolders (“YYYY-MM”) with "zoomed" graphs for each day of each month (“DD.json”). The subfolders with daily graphs are required only for those aiming to achieve the bonus goal.

Compared to Stage 1, we added a few new parameters and chart types into the JSONs.

chart.columns – List of all data columns in the chart. Each column has its label at position 0, followed by values.
x values are UNIX timestamps in milliseconds.

chart.types – Chart types for each of the columns. Supported values:
"line",
"area”,
"bar”,
"x" (x axis values for each of the charts at the corresponding positions).

```
chart.colors – Color for each variable in 6-hex-digit format (e.g. "#AAAAAA").
chart.names – Name for each variable.
chart.percentage – true for percentage based values.
chart.stacked – true for values stacking on top of each other.
chart.y_scaled – true for charts with 2 Y axes.
```

### 1. A line chart with 2 lines, exactly like in Stage 1 (Screenshot 1).

#### Bonus goal
A line chart with 2 lines that zooms into another line chart with 2 lines (Screenshot 2), as shown on the first video below.

### 2. A line chart with 2 lines and 2 Y axes (Screenshot 3).

#### Bonus goal:
A line chart with 2 Y axes that zooms into another line chart (Screenshot 2), as shown on the first video video demonstration below.

### 3. A stacked bar chart with 7 data types (Screenshots 5-6).
#### Bonus goal:
A stacked bar chart with 7 data types which zooms into another stacked bar chart with 7 data types.


### 4. A daily bar chart with single data type (Screenshot 7).
#### Bonus goal:
A daily bar chart with a single data type zooms into a line chart with 3 lines (the other two lines can represent values from 1 day and 1 week ago, as shown on Screenshot 8). Please see the second video demonstration below.
### 5. A percentage stacked area chart with 6 data types (Screenshots 9, 10).

#### Bonus goal:
A percentage stacked area chart with 6 data types that zooms into a pie chart with average values for the selected period (Screenshot 11). See the third video demonstration below.

## Note,
that you are not expected to implement the zooming transitions exactly as shown in the video demonstrations. They may be replaced with any slick and fast transition of your choice.

The Y-scale on line graphs should start with the lowest visible value (Screenshot 4). A long tap on any data filter should uncheck all other filters.

As in Stage 1, we will provide input data for all 5 graphs within the next 24 hours. We’ll also be updating you on the testing process, which, as mentioned before, will be public.

My solution implemented with Kotlin and Canvas
