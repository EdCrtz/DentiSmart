<template>
  <v-container>
    <v-select
      v-model="consultorio"
      :items="consultorios"
      item-value="id"
      item-text="nombre"
      label="Consultorio"
      placeholder="Seleccione un consultorio"
      outlined
      dense
      :menu-props="{ bottom: true, offsetY: true }"
      @change="getServicios()"
      v-if="user.role == 'ADMIN'"
    ></v-select>
    <v-data-table
      :headers="headers"
      :items="servicios"
      sort-by="nombre"
      class="elevation-1"
      :search="search"
    >
      <template v-slot:top>
        <v-toolbar color="azulSecundario" dark flat>
          <v-toolbar-title>Servicios</v-toolbar-title>
          <v-spacer></v-spacer>
          <v-row>
            <v-col :cols="$vuetify.breakpoint.mobile ? 10 : 12">
              <v-text-field
                v-model="search"
                append-icon="mdi-magnify"
                label="Buscar servicio"
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
                :disabled="!consultorio"
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
                          label="Nombre del servicio"
                          :rules="rules.nombre"
                          required
                        ></v-text-field>
                      </v-col>
                      <v-col cols="12">
                        <v-text-field
                          v-model="editedItem.descripcion"
                          label="Descripcion"
                          :rules="rules.descripcion"
                          required
                        ></v-text-field>
                      </v-col>
                      <v-col cols="12">
                        <v-text-field
                          v-model="editedItem.precio"
                          label="Precio"
                          :rules="rules.precio"
                          required
                        ></v-text-field>
                      </v-col>
                      <v-col cols="6">
                        <v-checkbox
                          v-model="editedItem.costoXDiente"
                          label="Aplica costo x diente"
                          color="primary"
                          hide-details
                        ></v-checkbox>
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
                >¿Desea borrar este servicio?</v-card-title
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
      <template v-slot:no-data v-if="user.role == 'ADMIN' && !consultorio">
        Debe seleccionar un consultorio
      </template>
    </v-data-table>
  </v-container>
</template>

<script>
import axios from "axios";
import { mapState } from "vuex";
import { environment } from "../env/environment";
export default {
  data: () => ({
    url: environment.url + "Servicio",
    dialog: false,
    dialogDelete: false,
    search: "",
    headers: [
      {
        text: "Nombre del Servicio",
        align: "start",
        sortable: false,
        value: "nombre"
      },
      { text: "Descripcion", value: "descripcion" },
      { text: "Precio", value: "precio" },
      { text: "Acciones", value: "actions", sortable: false }
    ],
    servicios: [],
    editedIndex: -1,
    editedItem: {
      nombre: "",
      descripcion: "",
      precio: 0,
      costoXDiente: false
    },
    defaultItem: {
      nombre: "",
      descripcion: "",
      precio: 0,
      costoXDiente: false
    },
    rules: {
      nombre: [val => (val || "").length > 0 || "Este campo es requerido"],
      descripcion: [val => (val || "").length > 0 || "Este campo es requerido"],
      precio: [val => val > 0 || "El precio debe ser mayor que cero"]
    },
    consultorios: [],
    consultorio: null
  }),

  computed: {
    ...mapState(["user"]),
    formTitle() {
      return this.editedIndex === -1
        ? "Agregar Nuevo Servicio"
        : "Editar Servicio";
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
    agregarServicio(servicio) {
      this.$vloading.show();
      axios
        .post(this.url, servicio)
        .then(() => {
          this.$vloading.hide();
          this.$toast.success("Se agrego el servicio correctamente");
          this.getServicios();
        })
        .catch(() => {
          this.$vloading.hide();
          this.$toast.error("No se pudo agregar el servicio");
        });
    },
    eliminarServicio(id) {
      this.$vloading.show();
      axios
        .delete(`${this.url}/${id}`)
        .then(() => {
          this.$vloading.hide();
          this.$toast.success("Se elimino el servicio correctamente");
          this.getServicios();
        })
        .catch(() => {
          this.$vloading.hide();
          this.$toast.error("No se pudo eliminar el servicio");
        });
    },
    actualizarServicio(servicio) {
      axios
        .put(this.url, servicio)
        .then(() => {
          this.$vloading.hide();
          this.$toast.success("Se actualizo el servicio correctamente");
          this.getServicios();
        })
        .catch(() => {
          this.$vloading.hide();
          this.$toast.error("No se pudo actualizar el servicio");
        });
    },
    getServicios() {
      this.$vloading.show();
      axios
        .get(this.url + "/Consultorio/" + this.consultorio)
        .then(response => {
          this.servicios = response.data;
          this.$vloading.hide();
        })
        .catch(() => {
          this.$vloading.hide();
          this.$toast.error("No se pudieron obtener servicios");
        });
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
    },
    initialize() {
      if (this.user.role == "ADMIN") {
        this.getConsultorios();
      } else if (this.user.role == "Dentista") {
        this.consultorio = this.user.consultorio;
        this.getServicios();
      }
    },

    editItem(item) {
      this.editedIndex = this.servicios.indexOf(item);
      this.editedItem = Object.assign({}, item);
      this.dialog = true;
    },

    deleteItem(item) {
      this.editedIndex = this.servicios.indexOf(item);
      this.editedItem = Object.assign({}, item);
      this.dialogDelete = true;
    },

    deleteItemConfirm() {
      this.servicios.splice(this.editedIndex, 1);
      this.eliminarServicio(this.editedItem.id);
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
        const servicio = {
          id: this.editedItem.id,
          nombre: this.editedItem.nombre,
          descripcion: this.editedItem.descripcion,
          precio: parseFloat(this.editedItem.precio),
          consultorio: this.consultorio,
          costoXDiente: this.editedItem.costoXDiente
        };
        if (this.editedIndex > -1) {
          this.actualizarServicio(servicio);
        } else {
          this.agregarServicio(servicio);
        }
        this.close();
      }
    }
  }
};
</script>
<style lang="scss" scoped>
.theme--dark.v-btn.v-btn--disabled:not(.v-btn--flat):not(.v-btn--text):not(.v-btn--outlined) {
  background-color: gray !important;
}
</style>
