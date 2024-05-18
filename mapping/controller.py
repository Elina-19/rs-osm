from flask import Flask, request
from search import search_entities
from flask_cors import CORS
from flask_restful import Api, Resource
from flasgger import Swagger, swag_from

app = Flask(__name__)
CORS(app)

app.config['SWAGGER'] = {
    'title': 'Recommender System API',
    'description': 'API for getting linked entities with the optimum variant',
    'uiversion': 3
}
swagger = Swagger(app)
api = Api(app)


class SearchResource(Resource):
    @swag_from({
        'tags': ['Search'],
        'summary': 'Search entities'
    })
    def get(self):
        query = request.args.get('query')
        if not query:
            return {"error": "Query parameter is required"}, 400

        result = search_entities(query)

        return result, 200


class SearchByUrlResource(Resource):
    @swag_from({
        'tags': ['Search'],
        'summary': 'Search linked entities with optimum variant'
    })
    def get(self):
        url = request.args.get('url')
        if not url:
            return {"error": "URL parameter is required"}, 400

        result = [
            {
                "url": "https://market.yandex.ru/product--akkumuliator-dlia-spetstekhniki-mutlu-sfb-3-l3-75-072-b-278x175x190/1772786445/spec?track=char",
                "name": "заглушка",
                "crawling_date": "2024-05-18 23:55:57"
            }
        ]

        return result, 200


api.add_resource(SearchResource, '/search')
api.add_resource(SearchByUrlResource, '/search-sim')


if __name__ == '__main__':
    app.run()
