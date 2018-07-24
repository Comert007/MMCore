## Core
the core is a tool library!


## 常用GIT
>列出常用 `git`使用方式，方便即是查询

* 删除分支: `git push origin --delete 分支名` 
* 修改远程分支: `git remote set-url origin "https://..."`
* 切换分支: `git checkout 分支名`
* 设置用户信息：`git config --global user.name "太平"`   
    `git config --global user.email "xxx@xx.com"`
* 克隆项目： `git clone "https://..."`
* 添加文件: `git add 文件名`
* 添加commit信息: `git commit -m "xxxx"`
* 添加项目下所有文件: `git add . 或 git add -A`
* 更新本地文件到远程: `git push -u origin master`

**上传整个仓库到新的git地址**
* 对origin 重命名： `git remote rename origin old-origin`
* 添加git地址 ： `git remote add origin "https://gitlab.xxx.net/xxx/xxx.git"` 
* 长传到新git地址 `git push -u origin --all`


**修改ignore文件并生效**
* `git rm -r --cached .`
* `git add .`
* `git commit -m "update .gitignore"`