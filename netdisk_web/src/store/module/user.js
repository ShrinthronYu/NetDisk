import { checkUserLoginInfo } from '@/request/user.js' // 引入获取用户登录信息接口

export default {
    state: {
        isLogin: false, // 初始时候给一个 isLogin = false 表示用户未登录
        userName: '',
        userId: 0,
        userImgUrl: '',
        userInfoObj: {}
    },
    mutations: {
        changeLogin(state, data) {
            state.isLogin = data
        },
        changeUserName(state, data) {
            state.userName = data
        },
        changeUserId(state, data) {
            state.userId = data
        },
        changeUserInfoObj(state, data) {
            state.userInfoObj = Object.assign({}, state.userInfoObj, data)
        }
    },
    actions: {
        getUserInfo(context) {
            return checkUserLoginInfo().then((res) => {
                if (res.success) {
                    context.commit('changeLogin', res.success)
                    context.commit('changeUserName', res.data.userName)
                    context.commit('changeUserId', res.data.userId)
                    context.commit('changeUserInfoObj', res.data)
                } else {
                    context.commit('changeLogin', res.success)
                }
            });
        }
    }
}