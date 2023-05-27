(function() {

    var graph = new joint.dia.Graph;
    var paper = new joint.dia.Paper({ el: $('#paper-html-elements'), width:700, height: 400, gridSize: 1, model: graph });

// 创建一个自定义元素
// ------------------------

    joint.shapes.html = {};
    joint.shapes.html.Element = joint.shapes.basic.Rect.extend({
        defaults: joint.util.deepSupplement({
            type: 'html.Element',
            attrs: {
                rect: { stroke: 'none', 'fill-opacity': 0 }
            }
        }, joint.shapes.basic.Rect.prototype.defaults)
    });

// 为在页面显示HTML的div元素创建自定义视图。
// -------------------------------------------------------------------------

    joint.shapes.html.ElementView = joint.dia.ElementView.extend({

        template: [
            '<div class="html-element">',
            '<button class="delete">x</button>',
            '<label></label>',
            '<span></span>', '<br/>',
            '<select><option>--</option><option>Q</option><option>L</option><option>Y</option></select>',
            '<input type="text" value="I\'m HTML input" />',
            '</div>'
        ].join(''),

        initialize: function() {    //初始化
            _.bindAll(this, 'updateBox');
            joint.dia.ElementView.prototype.initialize.apply(this, arguments);

            this.$box = $(_.template(this.template)());
            // 防止paper向下处理指针.
            this.$box.find('input,select').on('mousedown click', function(evt) {
                evt.stopPropagation();
            });
            // 这是对输入变化做出反应并将输入数据存储在单元格模型中的一个例子。
            this.$box.find('input').on('change', _.bind(function(evt) {
                this.model.set('input', $(evt.target).val());
            }, this));
            this.$box.find('select').on('change', _.bind(function(evt) {
                this.model.set('select', $(evt.target).val());
            }, this));
            this.$box.find('select').val(this.model.get('select'));
            this.$box.find('.delete').on('click', _.bind(this.model.remove, this.model));
            // 基础模型发生更改时更新框位置
            this.model.on('change', this.updateBox, this);
            // 当模型获取从图中删除时，删除框。
            this.model.on('remove', this.removeBox, this);

            this.updateBox();
        },
        render: function() {
            joint.dia.ElementView.prototype.render.apply(this, arguments);
            this.paper.$el.prepend(this.$box);
            this.updateBox();
            return this;
        },
        updateBox: function() {
            // 设置框的位置和尺寸，以便它涵盖了JointJS元素
            var bbox = this.model.getBBox();
            // Example of updating the HTML with a data stored in the cell model.
            this.$box.find('label').text(this.model.get('label'));
            this.$box.find('span').text(this.model.get('select'));  //将从select获取的元素回显到span
            this.$box.css({
                width: bbox.width,
                height: bbox.height,
                left: bbox.x,
                top: bbox.y,
                transform: 'rotate(' + (this.model.get('angle') || 0) + 'deg)'
            });
        },
        removeBox: function(evt) {
            this.$box.remove();
        }
    });

// 创建JointJS元素并将它们添加到图形像往常一样。
// -----------------------------------------------------------

    var el1 = new joint.shapes.html.Element({
        position: { x: 80, y: 80 },
        size: { width: 170, height: 100 },
        label: 'I am HTML',
        select: 'L'
    });
    var el2 = new joint.shapes.html.Element({
        position: { x: 370, y: 160 },
        size: { width: 170, height: 100 },
        label: 'Me too',
        select: 'Y'
    });
    var l = new joint.dia.Link({
        source: { id: el1.id },
        target: { id: el2.id },
        attrs: { '.connection': { 'stroke-width': 5, stroke: '#34495E' } }
    });

    graph.addCells([el1, el2,l]);

}());