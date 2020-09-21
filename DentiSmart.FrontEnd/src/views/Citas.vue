<template>
  <v-row>
    <v-col sm="12" lg="3" class="mb-4 controls">
      <v-btn
        fab
        small
        absolute
        left
        color="primary"
        @click="$refs.calendar.prev()"
      >
        <v-icon dark>mdi-chevron-left</v-icon>
      </v-btn>
      <v-btn
        fab
        small
        absolute
        right
        color="primary"
        @click="$refs.calendar.next()"
      >
        <v-icon dark>mdi-chevron-right</v-icon>
      </v-btn>
      <br /><br /><br />
      <v-select
        v-model="type"
        :items="typeOptions"
        label="Tipo"
        hide-details
        outlined
        dense
      ></v-select>
      <v-checkbox v-model="dark" label="Modo oscuro" hide-details></v-checkbox>
      <v-checkbox
        v-model="shortIntervals"
        label="Nombres cortos"
        hide-details
      ></v-checkbox>
      <v-checkbox
        v-model="shortMonths"
        label="Nombres de mes cortos"
        hide-details
      ></v-checkbox>
      <v-checkbox
        v-model="shortWeekdays"
        label="Nombres de semana cortos"
        hide-details
      ></v-checkbox>
      <v-select
        v-model="color"
        :items="colorOptions"
        class="mt-3"
        label="Color"
        hide-details
        outlined
        dense
      ></v-select>
      <v-menu
        ref="startMenu"
        v-model="startMenu"
        :close-on-content-click="false"
        :nudge-right="40"
        :return-value.sync="start"
        transition="scale-transition"
        min-width="290px"
        offset-y
      >
        <template v-slot:activator="{ on, attrs }">
          <v-text-field
            v-model="start"
            class="mt-3"
            label="Fecha inicio"
            dense
            readonly
            outlined
            hide-details
            v-bind="attrs"
            v-on="on"
          ></v-text-field>
        </template>
        <v-date-picker v-model="start" no-title scrollable>
          <v-spacer></v-spacer>
          <v-btn text color="primary" @click="startMenu = false">
            Cancelar
          </v-btn>
          <v-btn text color="primary" @click="$refs.startMenu.save(start)">
            OK
          </v-btn>
        </v-date-picker>
      </v-menu>
      <v-menu
        v-if="hasEnd"
        ref="endMenu"
        v-model="endMenu"
        :close-on-content-click="false"
        :nudge-right="40"
        :return-value.sync="end"
        transition="scale-transition"
        min-width="290px"
        offset-y
      >
        <template v-slot:activator="{ on, attrs }">
          <v-text-field
            v-model="end"
            class="mt-3"
            label="Fecha fin"
            dense
            readonly
            outlined
            hide-details
            v-bind="attrs"
            v-on="on"
          ></v-text-field>
        </template>
        <v-date-picker v-model="end" no-title scrollable>
          <v-spacer></v-spacer>
          <v-btn text color="primary" @click="endMenu = false">
            Cancelar
          </v-btn>
          <v-btn text color="primary" @click="$refs.endMenu.save(end)">
            OK
          </v-btn>
        </v-date-picker>
      </v-menu>
      <v-menu
        ref="nowMenu"
        v-model="nowMenu"
        :close-on-content-click="false"
        :nudge-right="40"
        :return-value.sync="now"
        transition="scale-transition"
        min-width="290px"
        offset-y
      >
        <template v-slot:activator="{ on, attrs }">
          <v-text-field
            v-model="now"
            class="mt-3"
            label="Hoy"
            dense
            readonly
            outlined
            hide-details
            v-bind="attrs"
            v-on="on"
          ></v-text-field>
        </template>
        <v-date-picker v-model="now" no-title scrollable>
          <v-spacer></v-spacer>
          <v-btn text color="primary" @click="nowMenu = false">
            Cancelar
          </v-btn>
          <v-btn text color="primary" @click="$refs.nowMenu.save(now)">
            OK
          </v-btn>
        </v-date-picker>
      </v-menu>
      <v-select
        v-model="mode"
        :items="modeOptions"
        dense
        outlined
        hide-details
        class="mt-3"
        label="Modo Superpocision de citas"
      ></v-select>
      <v-select
        v-model="weekdays"
        :items="weekdaysOptions"
        dense
        outlined
        hide-details
        class="mt-3"
        label="Dias de la semana"
      ></v-select>
      <v-text-field
        v-if="type === 'custom-weekly'"
        v-model="minWeeks"
        dense
        outlined
        hide-details
        class="mt-3"
        label="Minimo de semana"
        type="number"
      ></v-text-field>
      <v-select
        v-if="hasIntervals"
        v-model="intervals"
        :items="intervalsOptions"
        dense
        outlined
        hide-details
        class="mt-3"
        label="Intervals"
      ></v-select>
      <v-select
        v-if="type === 'custom-daily'"
        v-model="maxDays"
        :items="maxDaysOptions"
        dense
        outlined
        hide-details
        class="mt-3"
        label="# de dias"
      ></v-select>
      <v-select
        v-if="hasIntervals"
        v-model="styleInterval"
        :items="styleIntervalOptions"
        dense
        outlined
        hide-details
        class="mt-3"
        label="Estlismo"
      ></v-select>
    </v-col>
    <v-col sm="12" lg="9" class="pl-4">
      <v-sheet height="600">
        <v-calendar
          ref="calendar"
          v-model="start"
          :type="type"
          :start="start"
          :end="end"
          :min-weeks="minWeeks"
          :max-days="maxDays"
          :now="now"
          :dark="dark"
          :weekdays="weekdays"
          :first-interval="intervals.first"
          :interval-minutes="intervals.minutes"
          :interval-count="intervals.count"
          :interval-height="intervals.height"
          :interval-style="intervalStyle"
          :show-interval-label="showIntervalLabel"
          :short-intervals="shortIntervals"
          :short-months="shortMonths"
          :short-weekdays="shortWeekdays"
          :color="color"
          :events="events"
          :event-overlap-mode="mode"
          :event-overlap-threshold="45"
          :event-color="getEventColor"
          @change="getEvents"
        ></v-calendar>
      </v-sheet>
    </v-col>
  </v-row>
</template>
<script>
const weekdaysDefault = [0, 1, 2, 3, 4, 5, 6];

const intervalsDefault = {
  first: 0,
  minutes: 60,
  count: 24,
  height: 48
};

const stylings = {
  // eslint-disable-next-line no-unused-vars
  default(interval) {
    return undefined;
  },
  workday(interval) {
    const inactive =
      interval.weekday === 0 ||
      interval.weekday === 6 ||
      interval.hour < 9 ||
      interval.hour >= 17;
    const startOfHour = interval.minute === 0;
    const dark = this.dark;
    const mid = dark ? "rgba(255,255,255,0.1)" : "rgba(0,0,0,0.1)";

    return {
      backgroundColor: inactive
        ? dark
          ? "rgba(0,0,0,0.4)"
          : "rgba(0,0,0,0.05)"
        : undefined,
      borderTop: startOfHour ? undefined : "1px dashed " + mid
    };
  },
  past(interval) {
    return {
      backgroundColor: interval.past
        ? this.dark
          ? "rgba(0,0,0,0.4)"
          : "rgba(0,0,0,0.05)"
        : undefined
    };
  }
};

export default {
  data: () => ({
    dark: false,
    startMenu: false,
    start: "2019-01-12",
    endMenu: false,
    end: "2019-01-27",
    nowMenu: false,
    minWeeks: 1,
    now: null,
    events: [],
    colors: [
      "blue",
      "indigo",
      "deep-purple",
      "cyan",
      "green",
      "orange",
      "grey darken-1"
    ],
    names: [
      "Meeting",
      "Holiday",
      "PTO",
      "Travel",
      "Event",
      "Birthday",
      "Conference",
      "Party"
    ],
    type: "month",
    typeOptions: [
      { text: "Dia", value: "day" },
      { text: "4 dias", value: "4day" },
      { text: "Semana", value: "week" },
      { text: "Mes", value: "month" },
      { text: "Dia Personalizado", value: "custom-daily" },
      { text: "Semana Perzonalizada", value: "custom-weekly" }
    ],
    mode: "stack",
    modeOptions: [
      { text: "Pila", value: "stack" },
      { text: "Columa", value: "column" }
    ],
    weekdays: weekdaysDefault,
    weekdaysOptions: [
      { text: "Domingo - Sabado", value: weekdaysDefault },
      { text: "Lun, Mie, Vie", value: [1, 3, 5] },
      { text: "Lun - Vie", value: [1, 2, 3, 4, 5] },
      { text: "Lun - Dom", value: [1, 2, 3, 4, 5, 6, 0] }
    ],
    intervals: intervalsDefault,
    intervalsOptions: [
      { text: "Default", value: intervalsDefault },
      {
        text: "Workday",
        value: { first: 16, minutes: 30, count: 20, height: 48 }
      }
    ],
    maxDays: 7,
    maxDaysOptions: [
      { text: "7 dias", value: 7 },
      { text: "5 dias", value: 5 },
      { text: "4 dias", value: 4 },
      { text: "3 dias", value: 3 }
    ],
    styleInterval: "default",
    styleIntervalOptions: [
      { text: "Default", value: "default" },
      { text: "Workday", value: "workday" },
      { text: "Past", value: "past" }
    ],
    color: "primary",
    colorOptions: [
      { text: "Primario", value: "primary" },
      { text: "Secundario", value: "secondary" },
      { text: "Defecto", value: "accent" },
      { text: "Rojo", value: "red" },
      { text: "Rosa", value: "pink" },
      { text: "Morado", value: "purple" },
      { text: "Morado oscuro", value: "deep-purple" },
      { text: "Indigo", value: "indigo" },
      { text: "Azul", value: "blue" },
      { text: "Azul claro", value: "light-blue" },
      { text: "Cyan", value: "cyan" },
      { text: "Verde azulado", value: "teal" },
      { text: "Verde", value: "green" },
      { text: "Verde claro", value: "light-green" },
      { text: "Lima", value: "lime" },
      { text: "Amarillo", value: "yellow" },
      { text: "Miel", value: "amber" },
      { text: "Narnja", value: "orange" },
      { text: "Narnja oscuro", value: "deep-orange" },
      { text: "Cafe", value: "brown" },
      { text: "Azul gris", value: "blue-gray" },
      { text: "Gris", value: "gray" },
      { text: "Negro", value: "black" }
    ],
    shortIntervals: true,
    shortMonths: false,
    shortWeekdays: false
  }),
  computed: {
    intervalStyle() {
      return stylings[this.styleInterval].bind(this);
    },
    hasIntervals() {
      return (
        this.type in
        {
          week: 1,
          day: 1,
          "4day": 1,
          "custom-daily": 1
        }
      );
    },
    hasEnd() {
      return (
        this.type in
        {
          "custom-weekly": 1,
          "custom-daily": 1
        }
      );
    }
  },
  methods: {
    viewDay({ date }) {
      this.start = date;
      this.type = "day";
    },
    getEventColor(event) {
      return event.color;
    },
    showIntervalLabel(interval) {
      return interval.minute === 0;
    },
    getEvents({ start, end }) {
      const events = [];

      const min = new Date(`${start.date}T00:00:00`);
      const max = new Date(`${end.date}T23:59:59`);
      const days = (max.getTime() - min.getTime()) / 86400000;
      const eventCount = this.rnd(days, days + 20);

      for (let i = 0; i < eventCount; i++) {
        const allDay = this.rnd(0, 3) === 0;
        const firstTimestamp = this.rnd(min.getTime(), max.getTime());
        const first = new Date(firstTimestamp - (firstTimestamp % 900000));
        const secondTimestamp = this.rnd(2, allDay ? 288 : 8) * 900000;
        const second = new Date(first.getTime() + secondTimestamp);

        events.push({
          name: this.names[this.rnd(0, this.names.length - 1)],
          start: first,
          end: second,
          timed: !allDay,
          color: this.colors[this.rnd(0, this.colors.length - 1)]
        });
      }

      this.events = events;
    },
    rnd(a, b) {
      return Math.floor((b - a + 1) * Math.random()) + a;
    }
  }
};
</script>
<style scoped>
.controls {
  position: relative;
}
</style>
