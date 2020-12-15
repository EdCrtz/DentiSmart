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
      @change="getProductos()"
      v-if="user.role == 'ADMIN'"
    ></v-select>
    <v-data-table
      :headers="headers"
      :items="productos"
      class="elevation-1"
      width="200"
      sort-by="nombre"
      :search="search"
    >
      <template v-slot:top>
        <v-toolbar color="azulSecundario" dark flat>
          <v-toolbar-title>Productos</v-toolbar-title>
          <v-spacer></v-spacer>
          <v-row>
            <v-col :cols="$vuetify.breakpoint.mobile ? 10 : 12">
              <v-text-field
                v-model="search"
                append-icon="mdi-magnify"
                label="Buscar producto"
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
                      <v-col>
                        <v-text-field
                          v-model="editedItem.nombre"
                          label="Nombre producto"
                          :rules="rules.nombre"
                          required
                        ></v-text-field>
                      </v-col>
                    </v-row>
                    <v-row>
                      <v-col>
                        <v-text-field
                          v-model="editedItem.descripcion"
                          label="Descripcion"
                        ></v-text-field>
                      </v-col>
                    </v-row>
                    <v-row>
                      <v-col>
                        <v-text-field
                          v-model="editedItem.precio"
                          label="Precio"
                          type="number"
                          :rules="rules.precio"
                          required
                        ></v-text-field>
                      </v-col>
                    </v-row>
                    <v-row>
                      <v-col>
                        <v-text-field
                          v-model="editedItem.cantidad"
                          label="Cantidad"
                          type="number"
                          :rules="rules.cantidad"
                          required
                        ></v-text-field>
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
                este producto?</v-card-title
              >
              <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn color="blue darken-1" text @click="closeDelete"
                  >Cancelar</v-btn
                >
                <v-btn color="blue darken-1" text @click="deleteItemConfirm"
                  >Borar</v-btn
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
    url: environment.url + "Producto",
    dialog: false,
    dialogDelete: false,
    headers: [
      {
        text: "Nombre",
        align: "start",
        value: "nombre"
      },
      { text: "Precio", value: "precio" },
      { text: "Cantidad", value: "cantidad" },
      { text: "Acciones", value: "actions", sortable: false }
    ],
    productos: [],
    editedIndex: -1,
    editedItem: {
      id: "",
      nombre: "",
      descripcion: "",
      precio: 0,
      cantidad: 0
    },
    defaultItem: {
      id: "",
      nombre: "",
      descripcion: "",
      precio: 0,
      cantidad: 0
    },
    search: "",
    rules: {
      precio: [val => val > 0 || "El precio debe ser mayor que cero"],
      cantidad: [val => val >= 0 || "La cantidad debe ser positiva"],
      nombre: [val => (val || "").length > 0 || "Este campo es requerido"]
    },
    consultorios: [],
    consultorio: null
  }),

  computed: {
    ...mapState(["user"]),
    formTitle() {
      return this.editedIndex === -1 ? "Nuevo producto" : "Editar producto";
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
    agregarProducto(producto) {
      this.$vloading.show();
      axios
        .post(this.url, producto)
        .then(() => {
          this.$vloading.hide();
          this.$toast.success("Se agrego el producto correctamente");
          this.getProductos();
        })
        .catch(() => {
          this.$vloading.hide();
          this.$toast.error("No se pudo agregar producto");
        });
    },
    eliminarProducto(id) {
      this.$vloading.show();
      axios
        .delete(`${this.url}/${id}`)
        .then(() => {
          this.$vloading.hide();
          this.$toast.success("Se elimino el producto correctamente");
          this.getProductos();
        })
        .catch(() => {
          this.$vloading.hide();
          this.$toast.error("No se pudo eliminar el producto");
        });
    },
    actualizarProducto(producto) {
      axios
        .put(this.url, producto)
        .then(() => {
          this.$vloading.hide();
          this.$toast.success("Se actualizo el producto correctamente");
          this.getProductos();
        })
        .catch(() => {
          this.$vloading.hide();
          this.$toast.error("No se pudo actualizar el producto");
        });
    },
    getProductos() {
      this.$vloading.show();
      axios
        .get(this.url + "/Consultorio/" + this.consultorio)
        .then(response => {
          this.productos = response.data;
          this.$vloading.hide();
        })
        .catch(() => {
          this.$vloading.hide();
          this.$toast.error("No se pudieron obtener productos");
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
      //this.getProductos();
      if (this.user.role == "ADMIN") {
        this.getConsultorios();
      } else if (this.user.role == "Dentista") {
        this.consultorio = this.user.consultorio;
        this.getProductos();
      }
    },
    editItem(item) {
      this.editedIndex = this.productos.indexOf(item);
      this.editedItem = Object.assign({}, item);
      this.dialog = true;
    },

    deleteItem(item) {
      this.editedIndex = this.productos.indexOf(item);
      this.editedItem = Object.assign({}, item);
      this.dialogDelete = true;
    },

    deleteItemConfirm() {
      this.productos.splice(this.editedIndex, 1);
      this.eliminarProducto(this.editedItem.id);
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
        const producto = {
          id: this.editedItem.id,
          nombre: this.editedItem.nombre,
          descripcion: this.editedItem.descripcion,
          precio: parseFloat(this.editedItem.precio),
          cantidad: parseInt(this.editedItem.cantidad),
          consultorio: this.consultorio
        };
        if (this.editedIndex > -1) {
          debugger;
          this.actualizarProducto(producto);
        } else {
          this.agregarProducto(producto);
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
