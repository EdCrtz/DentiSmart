<template>
  <v-container>
    <div>
      <v-text-field v-model="text" label="Agregar Nota"></v-text-field>
    </div>
    <v-btn color="primary" @click="agregarNota()">Agregar una nueva nota</v-btn>
    <v-list>
      <v-subheader>Por hacer</v-subheader>
      <v-list-item-group color="primary">
        <v-list-item v-for="(item, i) in lista" :key="i">
          <v-list-item-content>
            <v-list-item-title v-text="item"></v-list-item-title>
          </v-list-item-content>
        </v-list-item>
      </v-list-item-group>
    </v-list>
    <v-snackbar color="red" v-model="snackbar" :timeout="timeout">
      {{ mesagge }}
      <template v-slot:action="{ attrs }">
        <v-btn color="white" text v-bind="attrs" @click="snackbar = false">
          Cerrar
        </v-btn>
      </template>
    </v-snackbar>
  </v-container>
</template>
<script>
export default {
  name: "Productos",

  components: {},

  data: () => ({
    drawer: false,
    text: "",
    lista: [],
    snackbar: false,
    mesagge: "",
    timeout: 2000
  }),
  methods: {
    agregarNota() {
      if (this.text) {
        this.lista.push(this.text);
        this.text = "";
      } else {
        this.mesagge = "no debe estar vacio";
        this.snackbar = true;
      }
    }
  }
};
</script>
