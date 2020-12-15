<template>
  <div class="container">
    <v-data-table
      :headers="headers"
      :items="dentistas"
      sort-by="calories"
      class="elevation-1"
      :search="search"
    >
      <template v-slot:[`item.fullName`]="{ item }">
        {{ `${item.nombre} ${item.apellidoPat} ${item.apellidoMat}` }}
      </template>
      <template v-slot:top>
        <v-toolbar color="azulSecundario" dark flat>
          <v-toolbar-title>Dentistas</v-toolbar-title>

          <v-spacer></v-spacer>
          <v-row>
            <v-col :cols="$vuetify.breakpoint.mobile ? 10 : 12">
              <v-text-field
                v-model="search"
                append-icon="mdi-magnify"
                label="Buscar Dentista"
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
                  <v-form ref="form">
                    <v-row>
                      <v-col cols="12">
                        <v-text-field
                          v-model="editedItem.nombre"
                          label="Nombre"
                          :rules="rules.nombre"
                          required
                        ></v-text-field>
                      </v-col>
                      <v-col cols="12" sm="6">
                        <v-text-field
                          v-model="editedItem.apellidoPat"
                          label="Apellido Paterno"
                          :rules="rules.apellidoPat"
                          required
                        ></v-text-field>
                      </v-col>
                      <v-col cols="12" sm="6">
                        <v-text-field
                          v-model="editedItem.apellidoMat"
                          label="Apellido Materno"
                          :rules="rules.apellidoMat"
                        ></v-text-field>
                      </v-col>
                      <v-col cols="12" sm="6">
                        <v-text-field
                          v-model="editedItem.telefono"
                          label="Numero de telefono"
                          :rules="rules.telefono"
                          required
                        ></v-text-field>
                      </v-col>
                      <v-col cols="12" sm="6">
                        <v-text-field
                          v-model="editedItem.email"
                          label="Correo Electronico"
                          :rules="rules.email"
                        ></v-text-field>
                      </v-col>

                      <v-col cols="12">
                        <v-text-field
                          v-model="editedItem.nombreUsuario"
                          label="Nombre de usuario"
                          :rules="rules.nombreUsuario"
                          required
                        ></v-text-field>
                      </v-col>
                      <v-col cols="12">
                        <v-text-field
                          v-model="editedItem.contrasenia"
                          label="Contraseña"
                          :rules="rules.contrasenia"
                          required
                          type="password"
                        ></v-text-field>
                      </v-col>

                      <v-col cols="12">
                        <v-select
                          v-model="editedItem.consultorio"
                          :items="consultorios"
                          item-value="id"
                          item-text="nombre"
                          label="Consultorio"
                          placeholder="Seleccione un consultorio"
                          :rules="rules.consultorio"
                          required
                        ></v-select>
                      </v-col>
                      <v-col cols="6">
                        <v-dialog
                          ref="dialog"
                          v-model="modal3"
                          :return-value.sync="editedItem.horaInicio"
                          persistent
                          width="290px"
                        >
                          <template v-slot:activator="{ on, attrs }">
                            <v-text-field
                              v-model="editedItem.horaInicio"
                              label="Hora de Inicio"
                              prepend-icon="mdi-clock-time-four-outline"
                              readonly
                              v-bind="attrs"
                              v-on="on"
                              :rules="rules.horaInicio"
                              required
                            ></v-text-field>
                          </template>
                          <v-time-picker
                            v-if="modal3"
                            v-model="editedItem.horaInicio"
                            :allowed-minutes="allowedMinutes"
                            full-width
                          >
                            <v-spacer></v-spacer>
                            <v-btn text color="primary" @click="modal3 = false">
                              Cancelar
                            </v-btn>
                            <v-btn
                              text
                              color="primary"
                              @click="$refs.dialog.save(editedItem.horaInicio)"
                            >
                              Confirmar
                            </v-btn>
                          </v-time-picker>
                        </v-dialog>
                      </v-col>
                      <v-col cols="6">
                        <v-dialog
                          ref="dialog2"
                          v-model="modal2"
                          :return-value.sync="editedItem.horaFin"
                          persistent
                          width="290px"
                        >
                          <template v-slot:activator="{ on, attrs }">
                            <v-text-field
                              v-model="editedItem.horaFin"
                              label="Hora de Fin"
                              prepend-icon="mdi-clock-time-four-outline"
                              readonly
                              v-bind="attrs"
                              v-on="on"
                              :disabled="!editedItem.horaInicio"
                              :rules="rules.horaFin"
                              required
                            ></v-text-field>
                          </template>
                          <v-time-picker
                            v-if="modal2"
                            v-model="editedItem.horaFin"
                            full-width
                            :min="editedItem.horaInicio"
                            :allowed-minutes="allowedMinutes"
                          >
                            <v-spacer></v-spacer>
                            <v-btn text color="primary" @click="modal2 = false">
                              Cancelar
                            </v-btn>
                            <v-btn
                              text
                              color="primary"
                              @click="$refs.dialog2.save(editedItem.horaFin)"
                            >
                              Confirmar
                            </v-btn>
                          </v-time-picker>
                        </v-dialog>
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
                este Dentista?</v-card-title
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
  </div>
</template>

<script>
import axios from "axios";
import { environment } from "../env/environment";
export default {
  data: () => ({
    url: environment.url + "Usuario",
    dialog: false,
    dialogDelete: false,
    headers: [
      { text: "Nombre Completo", value: "fullName", sortable: true },
      { text: "Nombre de usuario", value: "nombreUsuario" },
      { text: "Numero de teléfono", value: "telefono" },
      { text: "Correo electrónico", value: "email" },
      { text: "Consultorio", value: "datosConsultorio.nombre" },
      { text: "Acciones", value: "actions", sortable: false }
    ],
    dentistas: [],
    time: null,
    modal3: false,
    modal2: false,
    search: "",
    editedIndex: -1,
    editedItem: {
      id: "",
      nombre: "",
      apellidoPat: "",
      apellidoMat: "",
      nombreUsuario: "",
      contrasenia: "",
      email: "",
      role: "",
      telefono: "",
      foto: null,
      consultorio: "",
      horaInicio: null,
      horaFin: null
    },
    defaultItem: {
      id: "",
      nombre: "",
      apellidoPat: "",
      apellidoMat: "",
      nombreUsuario: "",
      contrasenia: "",
      email: "",
      role: "",
      telefono: "",
      foto: null,
      consultorio: "",
      horaInicio: null,
      horaFin: null
    },
    consultorios: [],
    rules: {
      nombre: [val => (val || "").length > 0 || "Este campo es requerido"],
      apellidoPat: [val => (val || "").length > 0 || "Este campo es requerido"],
      telefono: [
        val => (val || "").length > 0 || "El telefono es requerido",
        val =>
          (val || "").length == 10 ||
          "El telefono solo debe contener 10 caracteres"
      ],
      nombreUsuario: [
        val => (val || "").length > 0 || "Nombre de usuario es requerido"
      ],
      contrasenia: [
        val => (val || "").length > 0 || "La contraseña es requerida"
      ],
      consultorio: [
        val => (val || "").length > 0 || "El consultorio es requeirido"
      ],
      horaInicio: [
        val =>
          (val || "").length > 0 || "la hora de atencion inicial es requerida"
      ],
      horaFin: [
        val =>
          (val || "").length > 0 || "la hora de atencion final es requerida"
      ]
    }
  }),

  computed: {
    formTitle() {
      return this.editedIndex === -1 ? "Nuevo Dentista" : "Editar Dentista";
    }
  },

  watch: {
    dialog(val) {
      if (!val) {
        this.close();
        this.$refs.form.reset();
      }
    },
    dialogDelete(val) {
      val || this.closeDelete();
    }
  },

  created() {
    this.initialize();
  },

  methods: {
    allowedMinutes: v => v % 30 == 0,
    obtenerDentistas() {
      this.$vloading.show();
      axios
        .get(this.url + "/role/Dentista")
        .then(res => {
          this.dentistas = res.data;
          this.$vloading.hide();
        })
        .catch(() => {
          this.$vloading.hide();
          this.$toast.error("No se pudieron obtener los Dentistas");
        });
    },

    agregarDentista(dentista) {
      this.$vloading.show();
      axios
        .post(this.url, dentista)
        .then(() => {
          this.$vloading.hide();
          this.$toast.success("Se agrego el dentista correctamente");
          this.obtenerDentistas();
        })
        .catch(() => {
          this.$vloading.hide();
          this.$toast.error("Error: No se pudo agregar al dentista ");
        });
    },
    editarDentista(dentista) {
      axios
        .put(this.url, dentista)
        .then(() => {
          this.$vloading.hide();
          this.$toast.success(
            "Se actualizaron los datos del dentista correctamente"
          );
          this.obtenerDentistas();
        })
        .catch(() => {
          this.$vloading.hide();
          this.$toast.error("No se pudieron actualizar los datos del dentista");
        });
    },
    eliminarDentista(id) {
      this.$vloading.show();
      axios
        .delete(`${this.url}/${id}`)
        .then(() => {
          this.$vloading.hide();
          this.$toast.success("Se elimino el dentista correctamente");
          this.obtenerDentistas();
        })
        .catch(() => {
          this.$vloading.hide();
          this.$toast.error("No se pudo eliminar al dentista ");
        });
    },
    initialize() {
      this.obtenerDentistas();
      this.getConsultorios();
    },

    editItem(item) {
      this.editedIndex = this.dentistas.indexOf(item);
      this.editedItem = Object.assign({}, item);
      this.dialog = true;
    },

    deleteItem(item) {
      this.editedIndex = this.dentistas.indexOf(item);
      this.editedItem = Object.assign({}, item);
      this.dialogDelete = true;
    },

    deleteItemConfirm() {
      this.dentistas.splice(this.editedIndex, 1);
      this.eliminarDentista(this.editedItem.id);
      this.closeDelete();
    },

    close() {
      this.dialog = false;
      this.$nextTick(() => {
        this.editedItem = Object.assign({}, this.defaultItem);
        this.editedIndex = -1;
      });
    },

    closeDelete() {
      this.dialogDelete = false;
      this.$nextTick(() => {
        this.editedItem = Object.assign({}, this.defaultItem);
        this.editedIndex = -1;
      });
    },

    save() {
      if (this.$refs.form.validate()) {
        const dentista = {
          id: this.editedItem.id,
          nombre: this.editedItem.nombre,
          apellidoPat: this.editedItem.apellidoPat,
          apellidoMat: this.editedItem.apellidoMat,
          telefono: this.editedItem.telefono,
          email: this.editedItem.email,
          nombreUsuario: this.editedItem.nombreUsuario,
          role: "Dentista",
          contrasenia: this.editedItem.contrasenia,
          consultorio: this.editedItem.consultorio,
          horaInicio: this.editedItem.horaInicio,
          horaFin: this.editedItem.horaFin
        };

        if (this.editedIndex > -1) {
          this.editarDentista(dentista);
        } else {
          this.agregarDentista(dentista);
        }
        this.close();
      }
    },
    getConsultorios() {
      this.$vloading.show();
      axios
        .get(environment.url + "consultorio")
        .then(response => {
          this.consultorios = response.data;
          if (this.consultorios.length == 0) {
            this.$toast.info("No hay registros consultorios");
          }
          this.$vloading.hide();
        })
        .catch(() => {
          this.$vloading.hide();
          this.$toast.error("No se pudieron obtener los consultorios");
        });
    }
  }
};
</script>
