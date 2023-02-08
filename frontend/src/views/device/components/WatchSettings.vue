<template>
  <form-panel title="监护设备设置" :visible.sync="open" width="600px" append-to-body>
    <el-tabs v-model="currentTab">
      <el-tab-pane label="设备定位" name="first" >
        <el-row type="flex" justify="center">
            <span v-show="reLocating">获取机器定位中...</span>
            <span v-show="!reLocating">定位完毕</span>
        </el-row>
        <el-row type="flex" justify="center">
            <gao-de-map ref="gaoDeMap" style="height: 400px;width: 400px" v-model="location" :address.sync="address" :reLocating.sync="reLocating"></gao-de-map>
          </el-row>
        <el-row type="flex" justify="center">
          <el-card :body-style="{width:'400px'}">当前地址：{{address}}</el-card>
        </el-row>
        <el-row type="flex" justify="center">
          <el-button type="primary" @click="()=>this.$refs['gaoDeMap'].reLocateCurrentPosition()">立即获取定位</el-button>
        </el-row>
      </el-tab-pane>
      <el-tab-pane label="设置电子围栏" name="second">
        <el-row type="flex" justify="center">
          <gao-de-map v-if="currentTab == 'second'" v-model="location" show-circle="true" :circle-radius="radius" style="height: 400px;width: 400px" ></gao-de-map>
        </el-row>
        <el-row type="flex" justify="center">
          <el-card :body-style="{width:'400px'}">
            <el-row type="flex" justify="center">
              <el-input v-model="address" placeholder="请输入地址"></el-input>
            </el-row>
            <el-row type="flex" justify="center">
              <el-col :span="20"><span>设置半径长度</span></el-col>
              <el-col :span="4"><span>200米</span></el-col>
            </el-row>
            <el-row type="flex" justify="center">
              <el-col>
                <el-slider v-model="radius" style="width:90%" :min="1" :max="200"></el-slider>
              </el-col>
            </el-row>
          </el-card>
        </el-row>
      </el-tab-pane>
      <el-tab-pane label="设置联系电话" name="third">
              <el-card header="联系电话列表" :body-style="{width:'100%'}">
                <el-form :model="dynamicPhoneForm" ref="dynamicPhoneForm"  label-width="100px" class="demo-dynamic">
                  <el-form-item
                          v-for="(phone, index) in dynamicPhoneForm.phones"
                          :label="'联系人' + index"
                          :key="phone.key"
                          :prop="'phones.' + index + '.value'"
                          :rules="{
                            required: true, message: '联系人', trigger: 'blur'
                          }"
                        >
                        <el-col :span="12">
                          <el-input v-model="phone.value"></el-input>
                        </el-col>  
                        <el-col :span="10">
                          <el-button  type="primary" @click.prevent="removeDomain(phone)">删除</el-button>
                          <el-button  type="primary" @click="handleAdd">设置</el-button>
                        </el-col>
                      </el-form-item>
                </el-form>
              </el-card>
        <el-row type="flex" justify="center" style="margin-top: 10px">
              <!-- <el-button type="primary" @click="addRoomZones" :disabled="this.settings.roomZones.length >=4"> + 添加联系人</el-button> -->
              <el-button type="primary" @click="addDomain"  icon="el-icon-plus">添加联系人</el-button>

        </el-row>

      </el-tab-pane>
    </el-tabs>
    <div slot="footer" class="dialog-footer">
      <el-button type="primary"  @click="submitForm">提 交</el-button>
      <el-button @click="open = false">取 消</el-button>
      <!-- <el-button type="primary" icon="el-icon-plus"  @click="handleAdd">添加联系人</el-button>
      <el-button @click="open = false">取 消</el-button> -->
      
    </div>

    <el-dialog :title="title" :visible.sync="setPhone" width="500px" append-to-body>  
      <el-form :inline="true" label-width="125px">
          <el-row>        
            <el-col type="flex" justify="center">
              <el-form-item label="姓名" prop="name">
                <el-input v-model="form.name"  placeholder="请输入姓名" maxlength="16" />
              </el-form-item>
            </el-col>          
          </el-row>
          <el-row type="flex" justify="center" >
            <el-col>
              <el-form-item label="手机号码" prop="mobilePhone">
                <el-input v-model="form.mobilePhone" placeholder="请输入手机号码" maxlength="11" />
              </el-form-item>
            </el-col>          
          </el-row>   
          <el-row  class="text-center" type="flex">
              <el-radio-group v-model="form.phoneType" size="medium">
                <el-row class="text-center" type="flex"  v-for="(item, index) in setTypeListOptions">
                  <el-col :span="10">
                      <el-radio
                        :key="index"
                        :label="item.value"
                        :disabled="item.disabled"
                        >{{ item.label }}
                      </el-radio>
                     </el-col>
                  <!-- </div>  -->
              </el-row>
               </el-radio-group>
         </el-row>       
      </el-form> 


      <div slot="footer" class="dialog-footer">
        <el-button @click="setPhone = false">取 消</el-button>
      </div>
    </el-dialog>

  </form-panel>
</template>

<script>
export default {
  name: "WatchSettings",
  props: {
    settings: {
      type: Object,
      default(){
        return {
          type:"1"
        }
      }
    },
    visible: {
      type: Boolean,
      default: false
    }
  },
  data(){
    return {
      currentTab:'first',
      reLocating:false,
      address:"",
      location:undefined,
      radius:100,
      title:"",
      // 是否显示弹出层
      setPhone:false,
      // 表单参数
      form: {
        name:undefined,
        mobilePhone:undefined,
        phoneType:0,

      },

      setTypeListOptions:[
        { 
            value:0,
            label:'设置为SOS（默认）'
        },
        { 
            value:1,
            label:'设置为按钮1'
        },
        { 
            value:2,
            label:'设置为按钮2'
        },
        { 
            value:3,
            label:'设置为按钮3'
        },
        { 
            value:99,
            label:'暂保存，不设置'
        },

      ],
      dynamicPhoneForm: {
          phones: [{
            value: ''
          }],
          name:"SOS"
        }

    }
  },
  computed: {
    open: {
      get() {
        return this.visible;
      },
      set(val) {
        this.$emit('update:visible', val)
      }
    }
  },
  methods: {
    submitForm() {

    },
    handleAdd(){
      this.setPhone=true;
      this.title="设置联系人";
    },

    removeDomain(item) {
        var index = this.dynamicPhoneForm.phones.indexOf(item)
        if (index !== -1) {
          this.dynamicPhoneForm.phones.splice(index, 1)
        }
      },
      addDomain() {
        this.dynamicPhoneForm.phones.push({
          value: '',
          key: Date.now()
        });
      }

  }
}
</script>

<style scoped>

</style>
