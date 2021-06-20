<template>
  <div>
    <vs-row vs-justify="center">
      <vs-col type="flex" vs-justify="center" vs-align="center" :vs-lg="12" vs-sm="12" vs-xs="12"
              code-toggler>
        <vs-card class="cardx">
          <div slot="header">
            <h2 class="float-left" style="color: cornflowerblue">Projects List</h2>
            <div class="float-right mb-1">
              <vs-button @click="addForm = true" color="primary" icon="add" type="filled">Add New Project</vs-button>
            </div>
          </div>
          <div class="table-responsive">
            <table class="table v-middle border">
              <thead>
              <tr class="">
                <th class="border-top-0" style="color: cornflowerblue">ID</th>
                <th class="border-top-0" style="color: cornflowerblue">Name</th>
                <th class="border-top-0" style="color: cornflowerblue">Actions</th>
              </tr>
              </thead>
              <tbody>
              <tr v-for="project in projects" :key="project.id">
                <td>{{ project.id }}</td>
                <td>{{ project.name }}</td>
                <td>
                  <div>
                    <vs-button @click="currentProject = project; deleteForm = true" icon="delete" class="m-1"
                               color="danger" type="filled">
                      Delete
                    </vs-button>
                    <vs-button @click="editForm = true;editProject(project.id)" icon="edit" class="m-1" color="warning"
                               type="filled">
                      Edit
                    </vs-button>
                  </div>
                </td>
                <td>

                </td>
              </tr>
              </tbody>
            </table>
          </div>
        </vs-card>
      </vs-col>

      <vs-prompt
          title="Add New Project"
          color="primary"
          @cancel='  newName="" ;notify("Closed","Add was closed successfully","warning")'
          @accept="addProject() "
          @close=' newName=""; notify("Closed","Add was closed successfully","warning")'
          :is-valid="validProject()"
          :active.sync="addForm"
      >
        <div class="con-exemple-prompt">
          <vs-input class="mb-4" label-placeholder="Name" v-model="newName"/>
          <vs-alert
              :active="!validProject()"
              color="danger"
              icon="new_releases"
          >
            The name should not be empty
          </vs-alert>
        </div>
      </vs-prompt>

      <vs-prompt
          title="Edit Project"
          color="warning"
          @cancel='  oldName="" ;notify("Closed","Add was closed successfully","warning")'
          @accept="saveChanges()"
          @close=' oldName=""; notify("Closed","Add was closed successfully","warning")'
          :is-valid="validProject()"
          :active.sync="editForm"
      >
        <div class="con-exemple-prompt">
          <vs-input class="mb-4" label="Name" v-bind:placeholder="oldName" v-model="newName"/>
          <vs-alert
              :active="!validProject()"
              color="danger"
              icon="new_releases"
          >
            The name should not be empty
          </vs-alert>
        </div>
      </vs-prompt>
      <vs-prompt
          title="Deletion"
          color="red"
          @close='notify("Closed","Deletion was cancelled.","warning")'
          @cancel='notify("Closed","Deletion was cancelled.","warning")'
          @accept="deleteProject(currentProject.id)"
          :is-valid="true"
          :active.sync="deleteForm"
      >
        <div class="con-exemple-prompt">
          Are you sure you want to delete the Project <br>
          <h6>{{ currentProject.name }}</h6>
        </div>
      </vs-prompt>
    </vs-row>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: "Projects",
  data() {
    return {
      projects: [],
      editID: 0,
      currentProject: {},
      deleteForm: false,
      addForm: false,
      oldName: "",
      id: 1,
      editForm: false,
      newName: "",
    };
  },

  created() {
    this.fetchAllProjects()
  },


  methods: {
    saveChanges: async function () {
      await axios.put(`http://localhost:8080/api/v1/projects/` + this.editID, {id: this.editID, name: this.newName});
      this.newName = "";
      this.editID = 0;
      await this.fetchAllProjects();
    },
    deleteProject: async function (id) {
      // send DELETE request to API to delete a specific cat by ID
      this.projects = this.projects.filter((project) => project.id !== id);
      await axios.delete(`http://localhost:8080/api/v1/projects/` + id.toString()).then(() => {
        this.notify("Confirmation", "Project has been  successfully deleted", "danger")

      })
    },
    fetchAllProjects: async function () {
      await axios.get(`http://localhost:8080/api/v1/projects`).then((response) => {
        this.projects = response.data;
      });
    },

    validProject() {
      return this.newName.length !== 0
    },

    addProject: async function () {
      this.projects = this.projects.concat({
        id: this.id,
        name: this.newName,
      })
      this.id += 1;
      await axios.post(`http://localhost:8080/api/v1/projects`, {
        name: this.newName,
      })
      this.newName = "";
      await this.fetchAllProjects();
    },

    editProject: async function (id) {
      this.editID = id;
      await axios.get(`http://localhost:8080/api/v1/projects/` + id).then((response) => {
        this.oldName = response.data.name; // update the name

      })

    },
    /** Shows prompt with title, message and selected color*/
    notify: function (title, message, color) {
      this.$vs.notify({
        title: title,
        text: message,
        color: color, type: "gradient",
      })
    },



  }
}
</script>

<style scoped>
.edit-employeee {
  color: red;
}
</style>
