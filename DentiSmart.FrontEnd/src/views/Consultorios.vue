<template>
  <v-container>
    <v-data-table
      :headers="headers"
      :items="consultorios"
      class="elevation-1"
      :search="search"
    >
      <template v-slot:[`item.telefonosDos`]="{ item }">
        <p v-for="(item2, index) in item.telefonos" :key="index">
          {{ item2.tel }}
        </p>
      </template>
      <template v-slot:top>
        <v-toolbar color="azulSecundario" dark flat>
          <v-toolbar-title>Consultorios</v-toolbar-title>
          <v-spacer></v-spacer>
          <v-row>
            <v-col :cols="$vuetify.breakpoint.mobile ? 10 : 12">
              <v-text-field
                v-model="search"
                append-icon="mdi-magnify"
                label="Buscar consultorio"
                single-line
                hide-details
              ></v-text-field>
            </v-col>
          </v-row>

          <v-spacer></v-spacer>
          <v-dialog v-model="dialog" persistent max-width="500px">
            <template v-slot:activator="{ on, attrs }">
              <v-btn
                fab
                dark
                small
                absolute
                bottom
                right
                color="#0288d1"
                class="mb-2"
                v-bind="attrs"
                v-on="on"
              >
                <v-icon>mdi-plus</v-icon>
              </v-btn>
              <!-- <v-btn color="primary" dark class="mb-2" v-bind="attrs" v-on="on" elevation="24">
                        Nuevo consultorio
                    </v-btn> -->
            </template>
            <v-card>
              <v-toolbar color="azulSecundario" dark flat>
                <span class="headline">{{ formTitle }}</span>
                <v-spacer></v-spacer>
                <v-btn icon @click="close">
                  <v-icon>mdi-close</v-icon>
                </v-btn>
              </v-toolbar>

              <v-card-text>
                <v-container>
                  <!-- <v-row>
                                <v-col>
                                    <v-text-field v-model="editedItem.id" label="ID"></v-text-field>
                                </v-col>
                            </v-row> -->
                  <v-form ref="form">
                    <v-row>
                      <v-col>
                        <v-text-field
                          v-model="editedItem.nombre"
                          label="Nombre consultorio"
                          :rules="rules.nombre"
                          required
                        ></v-text-field>
                      </v-col>
                    </v-row>
                    <v-label>Direccion</v-label>
                    <v-row>
                      <v-col>
                        <v-text-field
                          v-model="editedItem.direccionConsultorio.calle"
                          label="Calle"
                          :rules="rules.calle"
                          required
                        ></v-text-field>
                      </v-col>
                    </v-row>
                    <v-row>
                      <v-col>
                        <v-text-field
                          v-model="editedItem.direccionConsultorio.numero"
                          label="Numero"
                          :rules="rules.numero"
                          required
                        ></v-text-field>
                      </v-col>
                    </v-row>
                    <v-row>
                      <v-col>
                        <v-text-field
                          v-model="editedItem.direccionConsultorio.depto"
                          label="Departamento"
                        ></v-text-field>
                      </v-col>
                    </v-row>
                    <v-row>
                      <v-col>
                        <v-text-field
                          v-model="editedItem.direccionConsultorio.colonia"
                          label="Colonia"
                          :rules="rules.colonia"
                          required
                        ></v-text-field>
                      </v-col>
                    </v-row>
                    <v-row>
                      <v-col cols="6">
                        <v-label>Telefonos</v-label>
                      </v-col>
                      <v-col cols="6">
                        <v-btn
                          fab
                          dark
                          small
                          bottom
                          right
                          color="#0288d1"
                          class="mb-2"
                          @click="nuevoTelefono"
                        >
                          <v-icon>mdi-plus</v-icon>
                        </v-btn>
                      </v-col>
                    </v-row>

                    <v-row v-for="(item2, index) in telefonos" :key="index">
                      <v-col cols="9">
                        <v-text-field
                          v-model="item2.tel"
                          label="Telefono"
                          type="number"
                          :rules="rules.tel"
                          required
                        ></v-text-field>
                      </v-col>
                      <v-col cols="3">
                        <v-btn icon @click="quitarTelefono(index)">
                          <v-icon>mdi-delete</v-icon>
                        </v-btn>
                      </v-col>
                    </v-row>
                  </v-form>
                </v-container>
              </v-card-text>

              <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn block color="blue darken-1" text @click="save">
                  Guardar
                </v-btn>
                <v-spacer></v-spacer>
              </v-card-actions>
            </v-card>
          </v-dialog>
          <v-dialog v-model="dialogDelete" max-width="500px">
            <v-card>
              <v-card-title class="headline"
                >¿Estas seguro que deseas borrar <br />
                este consultorio?</v-card-title
              >
              <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn color="blue darken-1" text @click="closeDelete"
                  >Cancelar</v-btn
                >
                <v-btn color="blue darken-1" text @click="deleteItemConfirm"
                  >Borrar</v-btn
                >
                <v-spacer></v-spacer>
              </v-card-actions>
            </v-card>
          </v-dialog>
        </v-toolbar>
      </template>
      <template v-slot:[`item.actions`]="{ item }">
        <v-icon small class="mr-2" @click="editItem(item)">
          mdi-pencil
        </v-icon>
        <v-icon small @click="deleteItem(item)">
          mdi-delete
        </v-icon>
      </template>
    </v-data-table>
  </v-container>
</template>

<script>
import axios from "axios";
import { environment } from "../env/environment";
export default {
  data: () => ({
    url: environment.url + "Consultorio",
    dialog: false,
    dialogDelete: false,
    search: "",
    rules: {
      nombre: [
        val =>
          (val || "").length > 0 || "El nombre del consultorio es requerido"
      ],
      calle: [
        val => (val || "").length > 0 || "La calle del consutorio es requerida"
      ],
      numero: [
        val => (val || "").length > 0 || "La calle del consutorio es requerida"
      ],
      colonia: [
        val =>
          (val || "").length > 0 || "La colonia del consutorio es requerida"
      ],
      tel: [
        val =>
          (val || "").length > 0 || "El telefono del consutorio es requerido"
      ]
    },
    headers: [
      {
        text: "Nombre",
        align: "start",
        value: "nombre"
      },
      {
        text: "Calle",
        value: "direccionConsultorio.calle",
        sortable: false
      },
      {
        text: "Telefonos",
        value: "telefonosDos",
        sortable: false
      },
      {
        text: "Acciones",
        value: "actions",
        sortable: false
      }
    ],
    consultorios: [],
    editedIndex: -1,
    editedItem: {
      id: "",
      nombre: "",
      direccionConsultorio: {
        calle: "",
        numero: "",
        depto: "",
        colonia: ""
      },
      telefonos: [
        {
          id: 0,
          tel: ""
        }
      ]
    },
    defaultItem: {
      id: "",
      nombre: "",
      direccionConsultorio: {
        calle: "",
        numero: "",
        depto: "",
        colonia: ""
      },
      telefonos: [
        {
          id: 0,
          tel: ""
        }
      ]
    },
    telefonos: [
      {
        id: 0,
        tel: ""
      }
    ]
  }),

  computed: {
    formTitle() {
      return this.editedIndex === -1
        ? "Nuevo consultorio"
        : "Editar consultorio";
    }
  },

  watch: {
    dialog(val) {
      if (!val) {
        this.close();
        this.$refs.form.resetValidation();
      }
    },
    dialogDelete(val) {
      val || this.closeDelete();
    }
  },

  created() {
    this.$vloading.show();
    this.initialize();
  },

  methods: {
    initialize() {
      this.obtenerConsultorios();
    },

    editItem(item) {
      this.editedIndex = this.consultorios.indexOf(item);
      this.editedItem = Object.assign({}, item);
      this.dialog = true;
      this.editedItem.direccionConsultorio = Object.assign(
        {},
        item.direccionConsultorio
      );

      for (let i = 0; i < this.editedItem.telefonos.length; i++) {
        this.telefonos[i] = {
          id: 0,
          tel: this.editedItem.telefonos[i].tel
        };
      }
      //this.telefonos = [...this.editItem.telefonos];
      //
    },

    deleteItem(item) {
      this.editedIndex = this.consultorios.indexOf(item);
      this.editedItem = Object.assign({}, item);
      this.dialogDelete = true;
    },
    quitarTelefono(index) {
      this.telefonos.splice(index, 1);
    },
    deleteItemConfirm() {
      // this.consultorios.splice(this.editedIndex, 1);
      this.eliminarConsultorio(this.editedItem.id);
      this.closeDelete();
    },

    close() {
      this.dialog = false;
      this.$nextTick(() => {
        this.editedItem = Object.assign({}, this.defaultItem);
        this.editedIndex = -1;
        this.telefonos = [
          {
            id: 0,
            tel: ""
          }
        ];
      });
    },

    closeDelete() {
      this.dialogDelete = false;
      this.$nextTick(() => {
        this.editedItem = Object.assign({}, this.defaultItem);
        this.editedIndex = -1;
        //this.$toast.success("Se eliminó el consultorio satisfactoriamente");
      });
    },
    nuevoTelefono() {
      this.telefonos.push({ id: 0, tel: "" });
    },
    save() {
      if (this.$refs.form.validate()) {
        this.$vloading.show();
        const consultorio = {
          id: this.editedItem.id,
          nombre: this.editedItem.nombre,
          direccionConsultorio: {
            calle: this.editedItem.direccionConsultorio.calle,
            numero: this.editedItem.direccionConsultorio.numero,
            depto: this.editedItem.direccionConsultorio.depto,
            colonia: this.editedItem.direccionConsultorio.colonia
          },
          telefonos: this.telefonos
        };

        if (this.editedIndex > -1) {
          // this.$toast.success("Editando consultorio");
          this.actualizarConsultorio(consultorio);
        } else {
          // this.$toast.success("Agregando nuevo consultorio");
          this.agregarConsultorio(consultorio);
        }
        this.initialize();
        this.close();
      }
    },
    actualizarConsultorio(consultorio) {
      axios
        .put(this.url, consultorio)
        .then(() => {
          this.$vloading.hide();
          this.$toast.success("Consultorioa actualizado correctamente");
          this.obtenerConsultorios();
        })
        .catch(() => {
          this.$vloading.hide();
          this.$toast.error("Error al actualizar el consultorio");
        });
    },
    agregarConsultorio(consultorio) {
      axios
        .post(this.url, consultorio)
        .then(() => {
          this.$vloading.hide();
          this.$toast.success("Consultorio agregado correctamente");
          this.obtenerConsultorios();
        })
        .catch(() => {
          this.$vloading.hide();
          this.$toast.error("Error al agregar el consultorio");
        });
    },
    eliminarConsultorio(id) {
      this.$vloading.show();

      axios
        .delete(`${this.url}/${id}`)
        .then(() => {
          this.$vloading.hide();
          this.obtenerConsultorios();
          this.$toast.success("Se ha eliminado correctamente");
        })
        .catch(() => {
          this.$vloading.hide();
          this.$toast.error("Error al eliminar el consultorio");
        });
    },

    obtenerConsultorios() {
      axios
        .get(this.url)
        .then(response => {
          this.consultorios = response.data;
          this.$vloading.hide();
        })
        .catch(() => {
          this.$toast.error("Error al cargar los consultorios");
          this.$vloading.hide();
        });
    }
  }
};
</script>
