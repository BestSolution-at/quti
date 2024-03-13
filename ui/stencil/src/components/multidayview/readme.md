# qutime-multidayview

<!-- Auto Generated Below -->


## Properties

| Property          | Attribute           | Description                                                                                              | Type                          | Default              |
| ----------------- | ------------------- | -------------------------------------------------------------------------------------------------------- | ----------------------------- | -------------------- |
| `days`            | `days`              | The number of days to show - defaults to 7                                                               | `number`                      | `7`                  |
| `events`          | `events`            | Array of events to show - might be encoded as a JSON-Array                                               | `readonly QEvent[] \| string` | `[]`                 |
| `hoursMax`        | `hours-max`         | End of hour grid, fractional digits are discarded and has to be in range of 0 - 24 - defaults to 24      | `number`                      | `24`                 |
| `hoursMin`        | `hours-min`         | Start of hour grid, fractional digits are discarded and has to be in range of 0 - 24 - defaults to 0     | `number`                      | `0`                  |
| `startDate`       | `start-date`        | The start date - defaults to start of the current week                                                   | `string`                      | `defaultStartDate()` |
| `workingHoursMax` | `working-hours-max` | End of work hours, fractional digits are discarded and has to be in range of 0 - 24 - defaults to 17     | `number`                      | `17`                 |
| `workingHoursMin` | `working-hours-min` | Start of working hours, fractional digits are discarded and has to be in range of 0 - 24 - defaults to 8 | `number`                      | `8`                  |


## Events

| Event        | Description                                        | Type                                                                                                                                                                                           |
| ------------ | -------------------------------------------------- | ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| `eventBlur`  | Event emitted when an event entry lost focus       | `CustomEvent<{ readonly key: string; readonly start: string; readonly end: string; readonly subject: string; readonly fullday?: boolean \| undefined; readonly type?: string \| undefined; }>` |
| `eventFocus` | Event emitted when an event entry has gained focus | `CustomEvent<{ readonly key: string; readonly start: string; readonly end: string; readonly subject: string; readonly fullday?: boolean \| undefined; readonly type?: string \| undefined; }>` |


----------------------------------------------

*Built with [StencilJS](https://stenciljs.com/)*
