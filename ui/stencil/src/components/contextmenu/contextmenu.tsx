import { Component, Prop, State, h } from '@stencil/core';

export type QMenuItem = {
  readonly label: string;
};

export type QMenuSection = {
  readonly label: string;
  readonly items: QMenuItem[];
};

@Component({
  tag: 'quti-context-menu',
  styleUrl: 'contextmenu.css',
  shadow: true,
})
export class QutiContextMenu {
  @Prop()
  public items: QMenuItem[] | QMenuSection[] = [];

  @State()
  private open: boolean;

  render() {
    return <div></div>;
  }
}
