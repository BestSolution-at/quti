# Quti Typescript/JavaScript Client

This project contains the client libary to access the Quti-API from Typescript/JavaScript

## Usage

```typescript
import { impl } from '@bestsolution/quti-calendar';

const calendarService = impl.createCalendarService(
	{
		baseUrl: 'http://127.0.0.1:8083',
	}
);
const [calendar, err] = await calendarService.get("7c1b0f1f-1a1a-4b05-b746-622d94e1c84b");
// ...
```
