/* eslint-disable */
/* tslint:disable */
/**
 * This is an autogenerated file created by the Stencil compiler.
 * It contains typing information for all components that exist in this project.
 */
import { HTMLStencilElement, JSXBase } from "@stencil/core/internal";
import { Events, LocalDates } from "./components/qutime-multidayview/qutime-multidayview";
export { Events, LocalDates } from "./components/qutime-multidayview/qutime-multidayview";
export namespace Components {
    interface QutimeMultidayview {
        "dates": LocalDates;
        "events": Events;
        "hoursMax": number;
        "hoursMin": number;
        "workingHoursMax": number;
        "workingHoursMin": number;
    }
}
declare global {
    interface HTMLQutimeMultidayviewElement extends Components.QutimeMultidayview, HTMLStencilElement {
    }
    var HTMLQutimeMultidayviewElement: {
        prototype: HTMLQutimeMultidayviewElement;
        new (): HTMLQutimeMultidayviewElement;
    };
    interface HTMLElementTagNameMap {
        "qutime-multidayview": HTMLQutimeMultidayviewElement;
    }
}
declare namespace LocalJSX {
    interface QutimeMultidayview {
        "dates"?: LocalDates;
        "events"?: Events;
        "hoursMax"?: number;
        "hoursMin"?: number;
        "workingHoursMax"?: number;
        "workingHoursMin"?: number;
    }
    interface IntrinsicElements {
        "qutime-multidayview": QutimeMultidayview;
    }
}
export { LocalJSX as JSX };
declare module "@stencil/core" {
    export namespace JSX {
        interface IntrinsicElements {
            "qutime-multidayview": LocalJSX.QutimeMultidayview & JSXBase.HTMLAttributes<HTMLQutimeMultidayviewElement>;
        }
    }
}